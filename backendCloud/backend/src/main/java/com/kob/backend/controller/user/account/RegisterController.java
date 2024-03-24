package com.kob.backend.controller.user.account;

import com.kob.backend.service.impl.user.account.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    RegisterServiceImpl registerService;

    @PostMapping("/account/register/")
    public Map<String,String> register (@RequestParam Map<String,String> param){
        return registerService.register(param.get("username"),param.get("password"),param.get("confirmPassword"));
    }
}
