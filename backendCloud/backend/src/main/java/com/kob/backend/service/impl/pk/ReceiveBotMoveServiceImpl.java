package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.Game;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if(WebSocketServer.users.get(userId) != null){
            Game game = WebSocketServer.users.get(userId).game;
            if(game != null)
                if(game.getPlayerA().getId().equals(userId)) {
                    if (!game.getPlayerA().getBotId().equals(-1)) //人工操作
                        game.setNextStepA(direction);
                }
                else if(game.getPlayerB().getId().equals(userId))
                    if(!game.getPlayerB().getBotId().equals(-1))
                        game.setNextStepB(direction);
        }
        return "receive bot move!";
    }
}
