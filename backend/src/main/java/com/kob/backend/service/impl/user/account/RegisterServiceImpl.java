package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String, String> map = new HashMap<>();
        if(username == null){
            map.put("error_message","用户名不能为空！");
            return map;
        }

        if(password == null || confirmPassword == null) {
            map.put("error_message", "密码不能为空！");
            return map;
        }

        if(password.isEmpty() || confirmPassword.isEmpty()) {
            map.put("error_message", "密码不能为空！");
            return map;
        }

        //删除空格
        username = username.trim();
        if(username.length() == 0){
            map.put("error_message", "用户名不合法！");
            return map;
        }

        if(username.length() > 100){
            map.put("error_message", "用户名过长！");
            return map;
        }

        if(password.length() > 100 || confirmPassword.length() > 100) {
            map.put("error_message", "密码长度过长！");
            return map;
        }

        if(!password.equals(confirmPassword)){
            map.put("error_message", "两次输入的密码不一致！");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);

        if(user != null){
            map.put("error_message", "用户名已存在！");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);

        String photo = "https://cdn.acwing.com/media/article/image/2022/07/07/1_d7f3b93efd-kob.png";

        User insertUser = new User(null,username,encodePassword,photo);

        userMapper.insert(insertUser);

        map.put("error_message", "success");

        return map;
    }
}
