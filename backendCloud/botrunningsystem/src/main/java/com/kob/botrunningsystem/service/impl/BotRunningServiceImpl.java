package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {
//        System.out.println(" input:"+input);
//        String[] inputs = input.split("#");
//        int[][] g = new int[13][14];
//        for (int i = 0; i < 13; i++) {
//            for (int j = 0; j < 14; j++)
//                g[i][j] = inputs[0].charAt(14 * i + j) - '0';
//            System.out.println(Arrays.toString(g[i]));
//        }
        botPool.addBot(userId,botCode,input);
        return "add bot success!";
    }
}
