package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.JWTToUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    /*
    静态变量对于整个类共享
    users维护所有的链接实例
    ConcurrentHashMap是线程安全的HashMap。专门用于多线程环境
    HashMap是线程不安全的，因为HashMap中操作都没有加锁，因此在多线程环境下会导致数据覆盖之类的问题，所以，在多线程中使用HashMap是会抛出异常的。
    ConcurrentHashMap是线程安全的，ConcurrentHashMap并非锁住整个方法，而是通过原子操作和局部加锁的方法保证了多线程的线程安全，且尽可能减少了性能损耗。
    ConcurrentHashMap代替同步的Map（Collections.synchronized(new HashMap()))，众所周知，HashMap是根据散列值分段存储的，同步Map在同步的时候锁住了所有的段，
    而ConcurrentHashMap加锁的时候根据散列值锁住了散列值锁对应的那段，因此提高了并发性能。
     */
    final public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    /*
    匹配池
    CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，
    然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，
    因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
     */
    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();

    //成员变量user代表当前实例
    private User user;
    private Session session;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private Game game = null;

    /*
    该server并不是springboot自带的，不能使用自动注入
     */
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    public static RecordMapper recordMapper;
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected");
        //解析token
        int userId = JWTToUserId.jwt2UserId(token);
        this.user = userMapper.selectById(userId);

        if(this.user != null)
            users.put(userId,this);
        else
            this.session.close();
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if(this.user != null){
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
    }

    private void startMatching(){
        matchPool.add(this.user);

        while (matchPool.size() >= 2){
            Iterator<User> iterator = matchPool.iterator();
            User a = iterator.next(), b = iterator.next();
            matchPool.remove(a);
            matchPool.remove(b);

            //匹配成功，新建一个Game类，该类维护玩家对战、地图创建，使用新的线程来执行
            Game game = new Game(13,14,30,a.getId(),b.getId());
            game.createMap();
            //将game对象与user连接绑定
            users.get(a.getId()).setGame(game);
            users.get(b.getId()).setGame(game);
            //执行game的线程run()
            game.start();

            JSONObject respGame = new JSONObject();
            respGame.put("a_id",game.getPlayerA().getId());
            respGame.put("a_sx",game.getPlayerA().getSx());
            respGame.put("a_sy",game.getPlayerA().getSy());
            respGame.put("b_id",game.getPlayerB().getId());
            respGame.put("b_sx",game.getPlayerB().getSx());
            respGame.put("b_sy",game.getPlayerB().getSy());
            respGame.put("gameMap",game.getG());

            JSONObject respA = new JSONObject();
            respA.put("event","matching-success");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("gameInfo",respGame);
            //在users连接池里面找到userA的链接，并发送其对手的信息
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event","matching-success");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("gameInfo",respGame);
            //在users连接池里面找到userA的链接，并发送其对手的信息
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void cancelMatching(){
        matchPool.remove(this.user);
    }

    private void move(Integer direction){
        if(game.getPlayerA().getId() == user.getId())
            game.setNextStepA(direction);
        else if(game.getPlayerB().getId() == user.getId())
            game.setNextStepB(direction);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
//        System.out.println(data.get("direction"));
        String event = data.getString("event");
        if(event.equals("start-matching"))
            startMatching();
        else if (event.equals("cancel-matching")) {
            cancelMatching();
        } else if (event.equals("move")) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
