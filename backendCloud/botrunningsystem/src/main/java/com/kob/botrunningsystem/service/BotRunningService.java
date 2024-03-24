package com.kob.botrunningsystem.service;

public interface BotRunningService {
    /*
    input为当前的地图信息
     */
    String addBot(Integer userId, String botCode, String input);
}
