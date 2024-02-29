package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper userMapper;
    @GetMapping("/all/")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }

    @GetMapping("/{userId}/")
    public User getUser(@PathVariable int userId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",0);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/add/{userId}/{username}/{password}/")
    public String addUser(@PathVariable int userId,@PathVariable String username,@PathVariable String password){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User user = userMapper.selectOne(queryWrapper);
        if(user != null)
            return "用户id已存在！";
        else
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user = new User(userId,username,passwordEncoder.encode(password));
            userMapper.insert(user);
            return "Add user successfully!";
        }
    }

    @GetMapping("/delete/{userId}/")
    public String deleteUser(@PathVariable int userId){
        userMapper.deleteById(userId);
        return "Delete User successfully!";
    }
}
