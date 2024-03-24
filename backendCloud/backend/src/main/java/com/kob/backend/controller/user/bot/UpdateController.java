package com.kob.backend.controller.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.impl.user.bot.UpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/bot")
@CrossOrigin(origins = "http://localhost:8080")
public class UpdateController {

    @Autowired
    private UpdateServiceImpl updateService;

    @PostMapping("/update/")
    public Map<String,String> update(@RequestParam Map<String,String> data){
        return updateService.update(data);
    }
}
