package com.github.walterfan;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
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

    //----------------------------------------------------------------------
    public String acquireLockWithTimeout(
            Jedis conn, String lockName, long acquireTimeout, long lockTimeout)
    {
        String identifier = UUID.randomUUID().toString();
        String lockKey = "lock:" + lockName;
        int lockExpire = (int)(lockTimeout / 1000);

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {
            if (conn.setnx(lockKey, identifier) == 1){
                conn.expire(lockKey, lockExpire);
                return identifier;
            }
            if (conn.ttl(lockKey) == -1) {
                conn.expire(lockKey, lockExpire);
            }

            try {
                Thread.sleep(1);
            }catch(InterruptedException ie){
                Thread.currentThread().interrupt();
            }
        }

        // null indicates that the lock was not acquired
        return null;
    }

    public boolean releaseLock(Jedis conn, String lockName, String identifier) {
        String lockKey = "lock:" + lockName;

        while (true){
            conn.watch(lockKey);
            if (identifier.equals(conn.get(lockKey))){
                Transaction trans = conn.multi();
                trans.del(lockKey);
                List<Object> results = trans.exec();
                if (results == null){
                    continue;
                }
                return true;
            }

            conn.unwatch();
            break;
        }

        return false;
    }

}
