package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    //自动注入
    @Autowired
    private LoginService loginService;

    @PostMapping("/account/token/")
    public Map<String,String> getToken(@RequestParam Map<String,String> map){   //表示以字典的形式传输过来的数据
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username,password);
    }
}
