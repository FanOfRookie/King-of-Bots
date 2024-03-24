package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.MatchService;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {
    @Override
    public String startGame(Integer aId,Integer aBotId, Integer bId,Integer bBotId) {
        System.out.println("Start Game :" +aId +" " + bId);
        WebSocketServer.startGame(aId,aBotId,bId,bBotId);
        WebSocketServer.userStatus.put(aId,"");
        WebSocketServer.userStatus.put(bId,"");
        return "Start Game :" +aId +" " + bId;
    }
}
