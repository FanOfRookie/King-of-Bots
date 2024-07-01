package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

 @Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    private final static String startGAME = "http://127.0.0.1:3000/pk/start/game/";

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer aId, Integer rating,Integer botId){
        lock.lock();
        try {
            players.add(new Player(aId,rating,botId,0));
        }finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer id){
        lock.lock();
        try {
            ArrayList<Player> newPlayers = new ArrayList<>();
            for (Player player : players)
                if(!player.getUserId().equals(id))
                    newPlayers.add(player);
            players = newPlayers;
        }finally {
            lock.unlock();
        }
    }

    private void increasingWaitingTime(){
        //所有玩家的等待时间加1
        for (Player player:players)
            player.setWaitingTime(player.getWaitingTime() + 1);
    }

    private boolean checkMatch(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(),b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void sendResult(Player a, Player b){
        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", String.valueOf(a.getUserId()));
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_id", String.valueOf(b.getUserId()));
        data.add("b_bot_id",b.getBotId().toString());
        restTemplate.postForObject(startGAME,data,String.class);
        System.out.println("send result:" + data.toString());
    }

    private void matchPlayers(){
        System.out.println("match players:" + players.toString());
        boolean[] used =new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if(used[i]) continue;
            for (int j = i + 1; j < players.size(); j++) {
                if(used[i]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if(checkMatch(a,b)){
                    used[i] = used[j] = true;
                    sendResult(a,b);
                    players.remove(a);
                    players.remove(b);
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increasingWaitingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println(e);
                break;
            }
        }
    }
}
