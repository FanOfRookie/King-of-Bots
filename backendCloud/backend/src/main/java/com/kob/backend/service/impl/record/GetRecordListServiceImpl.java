package com.kob.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer userId,Integer page,Integer pageSize) {
        IPage<Record> recordIPage = new Page<>(page,pageSize);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("game_id").eq("a_id",userId).or().eq("b_id",userId);
        List<Record> records = recordMapper.selectPage(recordIPage,queryWrapper).getRecords();

        JSONObject resp = new JSONObject();

        LinkedList<JSONObject> items = new LinkedList<>();
        for (Record record: records){
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo",userA.getPhoto());
            item.put("a_username",userA.getUsername());
            item.put("b_photo",userB.getPhoto());
            item.put("b_username",userB.getUsername());
            item.put("record",record);
            String result = "平局";
            if("A".equals(record.getLoser()))
                result = record.getAId().toString();
            else if ("B".equals(record.getLoser()))
                result = record.getBId().toString();
            item.put("loserId",result);
            items.add(item);
        }
        resp.put("records",items);
        resp.put("maxPage",(int)Math.ceil((double) recordMapper.selectCount(null) /(double) pageSize));

        return resp;
    }
}
