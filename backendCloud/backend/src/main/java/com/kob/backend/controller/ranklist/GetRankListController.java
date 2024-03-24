package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class GetRankListController {
    @Autowired
    private GetRankListService getRankListService;

    @GetMapping("/ranklist/getlist/")
    public JSONObject getRankList(@RequestParam Map<String,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        Integer pageSize = Integer.parseInt(data.getOrDefault("pageSize","10"));
        return getRankListService.getRankList(page,pageSize);
    }
}
