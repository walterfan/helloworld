package com.github.walterfan;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Created by yafan on 6/11/2017.
 */
public class RedisLock {

    private final JedisPool jedisPool;

    public RedisLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public boolean acquireLock(String key, int expire) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, "1", "NX", "PX", expire) != null;
        }
    }

    public boolean acquireLock(String key, int expire, int timeout) {
        Instant start = Instant.now();
        try {
            while (Instant.now().isBefore(start.plusMillis(timeout))) {
                if (acquireLock(key, expire)) {
                    return true;
                }
                TimeUnit.MILLISECONDS.sleep(2);

            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return false;
    }


    public boolean isLockInPlace(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key) != null;
        }
    }


    public boolean releaseLock(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key) == 1;
        }
    }
}
