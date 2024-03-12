package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/user/bot")
public class RemoveController {
    @Autowired
    private RemoveService service;

    @PostMapping("/remove/")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return service.remove(data);
    }
}
