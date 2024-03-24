package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/user/bot")
public class addController {
    @Autowired private AddService addService;

    @PostMapping("/add/")
    public Map<String,String> add(@RequestParam Map<String,String> data){
        return addService.add(data);
    }
}
