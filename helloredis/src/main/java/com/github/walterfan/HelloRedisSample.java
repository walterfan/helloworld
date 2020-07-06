package com.github.walterfan;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yafan on 4/4/2018.
 *
 * refer to http://www.baeldung.com/jedis-java-redis-client-library
 */
@Slf4j
public class HelloRedisSample {
    
    public static void operateString(Jedis jedis) {
        log.info("--- String Operation ---");
        jedis.set("author", "walter");
        log.info("The author is : " + jedis.get("author"));
    }

    public static void operateSet(Jedis jedis) {
        log.info("--- Set Operation ---");
        jedis.del("tasks");
        jedis.sadd("tasks", "task:1","task:2","task:3");

        Set<String> tasks = jedis.smembers("tasks");
        tasks.forEach(System.out::println);
        boolean exists = jedis.sismember("tasks", "task:1");
        log.info("task:1 " + exists);

    }

    public static void operateList(Jedis jedis) {
        log.info("--- List Operation ---");
        jedis.del("books");
        jedis.lpush("books", "Redis");
        jedis.lpush("books", "Mongodb");
        jedis.lpush("books", "Mysql");
        // Get the stored data and print it
        List<String> list = jedis.lrange("books", 0 ,5);

        list.forEach(System.out::println);
    }

    public static void operateHash(Jedis jedis) {
        log.info("--- Hash Operation ---");
        jedis.hset("user#1", "name", "Walter");
        jedis.hset("user#1", "job", "Engineer");

        String name = jedis.hget("user#1", "name");

        Map<String, String> fields = jedis.hgetAll("user#1");
        fields.entrySet().stream().forEach(e -> log.info("{}->{}", e.getKey(), e.getValue()));
    }

    public static void operateSortedSet(Jedis jedis) {
        log.info("--- SortedSet Operation ---");
        Map<String, Double> scores = new HashMap<>();

        scores.put("PlayerOne", 3000.0);
        scores.put("PlayerTwo", 1500.0);
        scores.put("PlayerThree", 8200.0);

        jedis.del("scores");
        scores.keySet().forEach(player -> {
            jedis.zadd("scores", scores.get(player), player);
        });

        String player = jedis.zrevrange("scores", 0, 1).iterator().next();
        long rank = jedis.zrevrank("scores", "PlayerOne");
    }


    public static void operateTransaction(Jedis jedis) {
        log.info("--- transaction Operation ---");
        //jedis.watch (key1, key2, ...);
        Transaction t = jedis.multi();
        t.set("festival", "tomb-sweeping");
        t.exec();
        log.info("festival: " + jedis.get("festival"));

    }

    public static void operatePipeline(Jedis jedis) {
        log.info("--- pipeline Operation ---");
        Pipeline p = jedis.pipelined();
        p.del("book-category");
        p.set("book-category", "pattern");
        p.del("patter-books");
        p.zadd("patter-books", 1, "posa1");
        p.zadd("patter-books", 0, "posa2");
        p.zadd("patter-books", 0, "posa3");

        Response<String> pipeString = p.get("book-category");
        Response<Set<String>> sose = p.zrange("patter-books", 0, -1);
        p.sync();

        int soseSize = sose.get().size();
        Set<String> setBack = sose.get();
        setBack.forEach(System.out::println);
    }

    public static void operatePubsub(Jedis jedis) {
        log.info("--- pubsub Operation ---");
        subscribeChannel(jedis);
        //jedis.publish("channel", "test message");

    }

    private static void subscribeChannel(final Jedis jedis) {
        Runnable task = new Runnable() {

            @Override
            public void run() {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        log.info("channel " + channel + ", message=" + message);
                    }
                }, "channel");
            }
        };
        new Thread(task).start();
    }

    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }
    
    public static void operationTest() {
        final JedisPoolConfig poolConfig = buildPoolConfig();
        try (JedisPool jedisPool = new JedisPool(poolConfig, "localhost")) {
            try (Jedis jedis = jedisPool.getResource()) {
                log.info("Connection to server sucessfully");
                operateString(jedis);
                operateList(jedis);
                operateHash(jedis);
                operateSet(jedis);
                operateSortedSet(jedis);
                operateTransaction(jedis);
                operatePipeline(jedis);
                operatePubsub(jedis);
            }  //return connection to pool
        } //close pool

    }
    
    public static void main(String[] args) {

        operationTest();
    }

}
