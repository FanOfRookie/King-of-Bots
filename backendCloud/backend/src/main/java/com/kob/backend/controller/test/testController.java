package com.kob.backend.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    private final Object lock = new Object();

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
//        redisTemplate.opsForValue().set("test","3");
            try {
                Thread currentThread = Thread.currentThread();
                System.out.println("ID: " + currentThread.getId());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        Object test = redisTemplate.opsForValue().get("test");
        return test == null ? "null" : test.toString();

    }
}
