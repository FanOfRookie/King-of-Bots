package com.kob.backend.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    private final Object lock = new Object();
    @GetMapping("/test")
    public String test(){
            try {
                Thread currentThread = Thread.currentThread();
                System.out.println("ID: " + currentThread.getId());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "finish";

    }
}
