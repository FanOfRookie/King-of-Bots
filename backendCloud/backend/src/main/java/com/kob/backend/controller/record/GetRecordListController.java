package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;

    @GetMapping("/record/getList/")
    private JSONObject getRecordList(@RequestParam Map<String,String> data){
        Integer userId = Integer.parseInt(data.get("userId"));
        Integer page = Integer.parseInt(data.get("page"));
        Integer pageSize = Integer.parseInt(data.getOrDefault("pageSize","10"));
        return getRecordListService.getList(userId,page,pageSize);
    }
}
