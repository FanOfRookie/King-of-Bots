package com.kob;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置Redis的连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用 GenericJackson2JsonRedisSerializer 作为值的序列化器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 使用 StringRedisSerializer 作为键的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
