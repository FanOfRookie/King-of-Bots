package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int botId = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(botId);
        HashMap<String, String> hashMap = new HashMap<>();
        if(bot == null){
            hashMap.put("error_message","bot不存在！");
            return hashMap;
        }

        if(!bot.getUserId().equals(user.getId())){
            hashMap.put("error_message","您没有权限删除该bot！");
            return hashMap;
        }else {
            botMapper.deleteById(botId);
            hashMap.put("error_message","success");
            return hashMap;
        }
    }
}
