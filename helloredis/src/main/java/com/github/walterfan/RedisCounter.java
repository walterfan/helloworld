package com.github.walterfan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * Created by yafan on 6/11/2017.
 */
@Component
public class RedisCounter {

    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;

    public long increase(String key, int count) {
        return longRedisTemplate.opsForValue().increment(key, count);
    }

    public long decrease(String key, int count) {
        return longRedisTemplate.opsForValue().increment(key, -count);
    }

    public long get(String key) {
        return longRedisTemplate.opsForValue().get(key);
    }
}