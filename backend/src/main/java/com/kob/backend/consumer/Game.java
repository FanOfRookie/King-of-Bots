package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    final private int rows;
    final private int cols;
    final private int innerWallNum;
    int[][] g;
    final static int[] dx = {-1,0,1,0}, dy = {0,1,0,-1};

    private final Player playerA, playerB;

    private String status = "playing";
    private String loser = "";

    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();
    public Integer getNextStepA() {
        return nextStepA;
    }


    //由于game线程与服务器的线程都会读写nextStep，为了防止读写冲突，需要上锁
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }
    }

    public Integer getNextStepB() {
        return nextStepB;
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }


    public Game(int rows,int cols, int innerWallNum, int idA, int idB){
        this.rows = rows;
        this.cols = cols;
        this.innerWallNum = innerWallNum;
        this.g = new int[rows][cols];
        playerA = new Player(idA,rows-2,1);
        playerB = new Player(idB,1,cols-2);
    }

    public int[][] getG(){
        return g;
    }

    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }

    //判断地图是否可用
    private boolean checkGraphConnectivity(int sx,int sy,int tx,int ty){
        if(sx == tx && sy == ty)
            return true;
        //将源点设为已遍历
        g[sx][sy]=1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            //x y未超限，且g[X][Y]未被访问
            if(x >= 0 && x < rows && y >= 0 && y < cols && g[x][y] == 0)
                if(checkGraphConnectivity(x,y,tx,ty)){
                    //由于g是全局变量，判断结束之后要恢复
                    g[sx][sy] = 0;
                    return true;
                }
        }

        //由于g是全局变量，判断结束之后要恢复
        g[sx][sy]=0;
        return false;
    }

    private boolean randomInitMap(){
        for (int i = 0; i < rows; i++)
            g[i][0] = g[i][cols - 1] = 1;
        for (int i = 0; i < cols; i++)
            g[0][i] = g[rows - 1][i] = 1;
        Random random = new Random();
        for (int i = 0; i < this.innerWallNum / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(rows);   // 0 ~ rows - 1
                int c = random.nextInt(cols);
                //重复点
                if(g[r][c] == 1 || g[rows - 1 - r][cols - 1 - c] == 1)
                    continue;
                //起始点
                if(r == rows - 2 && c == 1 || r == 1 && c == cols - 2)
                    continue;
                g[r][c] = g[rows - 1 - r][cols - 1 - c] = 1;
                break;
            }
        }
        return checkGraphConnectivity(rows-2,1,1,cols-2);
    }

    //随机创建一个地图
    public void createMap(){
        for (int i = 0; i < 10000; i++) {
            if(randomInitMap())
                break;
        }
    }

    //等待两名玩家的下一步操作
    private boolean nextStep(){
        //每次发送请求下一步之前等待200ms，
        //前端执行完当前的移动后才能接受下一次指令
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA != null && nextStepB!= null) {
                        playerA.addStep(nextStepA);
                        playerB.addStep(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void sendAllMessage(String message){
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB){
        int length = cellsA.size();
        Cell cell = cellsA.get(length - 1);
        if(g[cell.x][cell.y] == 1)  //撞墙
            return false;
        //判断A是否撞上自己
        for (Cell c: cellsA){
            if(c == cell)
                continue;
            if(c.x == cell.x && c.y == cell.y)
                return false;
        }
        //是否撞上B
        for (Cell c: cellsB){
            if(c.x == cell.x && c.y == cell.y)
                return false;
        }
        return true;
    }

    //判断两名玩家下一步操作合法性
    private void judge(){
        boolean validA = checkValid(playerA.cells,playerB.cells);
        boolean validB = checkValid(playerB.cells,playerA.cells);
        if(validA && validB)
            return;
        else if(!validA && !validB) {
            status = "finished";
            loser = "all";
        } else if (!validA) {
            status = "finished";
            loser = "A";
        }else {
            status = "finished";
            loser = "B";
        }
    }

    public void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    public void sendMove(){
        JSONObject resp = new JSONObject();
        resp.put("event","move");
        lock.lock();
        try {
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            nextStepB = nextStepA = null;
            sendAllMessage(resp.toJSONString());
        }finally {
            lock.unlock();
        }
    }

    private String getMapString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                builder.append(g[i][j]);
            }
        }
        return builder.toString();
    }

    private void saveToDatabase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerB.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getCellsString(),
                playerB.getCellsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    @Override
    public void run(){
        for (int i = 0; i < 1000; i++) {
            if(nextStep()){
                judge();
                if(status.equals("playing"))
                    sendMove();
                else {
                    sendResult();
                    System.out.println("===========1===============");
                    break;
                }
            }
            else{
                lock.lock();
                try {
                    status = "finished";
                    if(nextStepA == null && nextStepB == null)
                        loser = "all";  //平局
                    else if (nextStepA == null)
                        loser = "A";
                    else
                        loser = "B";
                }finally {
                    lock.unlock();
                }
                sendResult();
                System.out.println("==========2===============");
                //游戏结束
                break;
            }
        }
    }
}
