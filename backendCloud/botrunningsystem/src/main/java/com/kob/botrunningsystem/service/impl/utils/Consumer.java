package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }

    private final static String botMoveURL = "http://127.0.0.1:3000/pk/receive/bot/move/";

    Bot bot;

    public void startTimeout(long timeout, Bot bot){
        this.bot = bot;
        this.start();

        //线程执行完毕，或等待timeout时间后阻塞进程
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            this.interrupt();   //终止当前线程
        }
    }

    //在bot的类名后加uid
    private String addUid(String code,String uid){
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,8);
        //将代码以及包名传入Reflect进行编译，并通过get方法获取到编译后的类
        BotInterface botInterface = Reflect.compile("com.kob.botrunningsystem.utils.Bot"+uid,addUid(bot.botCode, uid)).create().get();
        Integer direction = botInterface.nextMove(bot.getInput());

        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", String.valueOf(bot.getUserId()));
        data.add("direction", String.valueOf(direction));
        restTemplate.postForObject(botMoveURL,data,String.class);
    }
}
