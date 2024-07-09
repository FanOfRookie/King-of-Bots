package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GetRankListServiceImpl implements GetRankListService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private final String RANK_LIST_CACHE_KEY = "RANK_LIST_CACHE_KEY";
    private final Integer RANK_LIST_CACHE_TIMEOUT = 60;

    @Override
    public JSONObject getRankList(Integer page,Integer pageSize, Integer userId) {
        JSONObject resp = new JSONObject();
        List<User> users = null;

        String distributeLock = RANK_LIST_CACHE_KEY + userId;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(distributeLock,userId,10,TimeUnit.SECONDS);
        if(lock == null || !lock) {
            log.info("compete query");
            return null;
        }
        try {
            users = getFromCache(page);
        }catch (Exception e){
            log.error(e + "# Redis Cache Server Disconnect!");
        }finally {
            if(Objects.equals(redisTemplate.opsForValue().get(distributeLock), userId))
                redisTemplate.delete(distributeLock);
            else
                System.out.println("transaction is not finished!");
        }

        if(users == null)
            users = getFromDB(page,pageSize);

        for (User user: users)
            user.setPassword("");
        resp.put("users",users);
        resp.put("maxPage",(int) Math.ceil((double) userMapper.selectCount(null) / pageSize));
        return resp;
    }

    private List<User> getFromDB(Integer page,Integer pageSize){
        log.info("query db");
        IPage<User> userIPage = new Page<>(page,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();

        Map<String, List<User>> cachedRankList = (Map<String, List<User>>) redisTemplate.opsForValue().get(RANK_LIST_CACHE_KEY);
        if(cachedRankList == null) {
            cachedRankList = new HashMap<>();
            cachedRankList.put(page.toString(), users);
        }
        else
            cachedRankList.put(page.toString(),users);
        redisTemplate.opsForValue().set(RANK_LIST_CACHE_KEY,cachedRankList,RANK_LIST_CACHE_TIMEOUT, TimeUnit.SECONDS);

        return users;
    }

    private List<User> getFromCache(Integer page){
        log.info("query cache");
        Map<String, List<User>> cachedRankList = (Map<String, List<User>>) redisTemplate.opsForValue().get(RANK_LIST_CACHE_KEY);
        return cachedRankList == null ? null : cachedRankList.get(page.toString());
    }
}
