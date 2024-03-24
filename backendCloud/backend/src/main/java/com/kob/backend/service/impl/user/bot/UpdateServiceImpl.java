package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserAuthentication;
import com.kob.backend.service.user.bot.UpdateService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        User user = UserAuthentication.getUser();
        HashMap<String, String> hashMap = new HashMap<>();
        String botId = data.get("bot_id");
        String content = data.get("content");
        String description = data.get("description");
        String title = data.get("title");
        Bot bot = botMapper.selectById(botId);

        if(title == null || title.isEmpty()) {
            hashMap.put("error_message", "标题不能为空！");
            return hashMap;
        }
        if(title.length() > 100) {
            hashMap.put("error_message", "标题长度不能大于100！");
            return hashMap;
        }

        if(content == null || content.isEmpty()) {
            hashMap.put("error_message", "Bot代码不能为空！");
            return hashMap;
        }
        if(content.length() > 10000) {
            hashMap.put("error_message", "Bot代码长度不能大于10000！");
            return hashMap;
        }
        if(description != null && description.length() > 300) {
            hashMap.put("error_message", "描述内容不能大于300字符！");
            return hashMap;
        }
        if(description == null ) {
            description = "主人很懒，什么都没有留下";
        }

        if(bot == null){
            hashMap.put("error_message","该bot不存在！");
            return hashMap;
        }

        if(!bot.getUserId().equals(user.getId())){
            hashMap.put("error_message","您没有权限修改该bot");
            return hashMap;
        }else {
            Date now = new Date();
            Bot updatedBot = new Bot(bot.getBotId(), user.getId(),
                    title, description, content, bot.getCreateTime(), now);
            botMapper.updateById(updatedBot);
            Bot modifyBot = botMapper.selectById(botId);
            hashMap.put("error_message","success");
            return hashMap;
        }
    }
}
