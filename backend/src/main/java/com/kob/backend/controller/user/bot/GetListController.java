package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/bot")
@CrossOrigin(origins = "http://localhost:8080")
public class GetListController {
    @Autowired
    private GetListService getListService;

    @GetMapping("/getBotList/")
    public List<Bot> getBotList(){
        return getListService.getList();
    }
}
