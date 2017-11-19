package com.github.walterfan;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

/**
 * Created by yafan on 18/11/2017.
 */

public class RedisCache {

    private final JedisPool jedisPool;


    public RedisCache(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Long hdel(String keyBase, String... fields) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hdel(keyBase, fields);
        }
    }

    public Long del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        }

    }

    public String hget(String keyBase, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(keyBase, key);
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public Set<String> hkeys(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hkeys(key);
        }
    }

    public Long hset(String keyBase, String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(keyBase, key, value);
        }
    }

    public String setex(String key, int seconds, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, seconds, value);
        }
    }

    public String set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        }
    }



    public static void main(String[] args) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(10);
        config.setMinIdle(1);
        config.setMaxWaitMillis(30000);

        try(JedisPool pool = new JedisPool(config, "localhost")) {
            RedisCache redisCache = new RedisCache(pool);
            redisCache.set("version", "1.0");

            String version = redisCache.get("version");
            System.out.println("version=" + version);
            assertThat("1.0".equals(version));

        }

    }
}
