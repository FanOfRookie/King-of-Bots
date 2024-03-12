package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        //通过token获取用户信息
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String title = data.get("title");
        String content = data.get("content");
        String description = data.get("description");

        HashMap<String, String> map = new HashMap<>();
        if(title == null || title.isEmpty()) {
            map.put("error_message", "标题不能为空！");
            return map;
        }
        if(title.length() > 100) {
            map.put("error_message", "标题长度不能大于100！");
            return map;
        }

        if(content == null || content.isEmpty()) {
            map.put("error_message", "Bot代码不能为空！");
            return map;
        }
        if(content.length() > 10000) {
            map.put("error_message", "Bot代码长度不能大于10000！");
            return map;
        }
        if(description != null && description.length() > 300) {
            map.put("error_message", "描述内容不能大于300字符！");
            return map;
        }
        if(description == null ) {
            description = "主人很懒，什么都没有留下";
        }

        Date now = new Date();
        Bot bot = new Bot(null,user.getId(),title,description,content,
                1500,now,now);

        botMapper.insert(bot);
        map.put("error_message", "success");
        return map;
    }
}
