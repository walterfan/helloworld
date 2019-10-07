package com.github.walterfan;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Author: Walter Fan
 * @Date: 5/9/2019, Thu
 **/
@Slf4j
public class LettuceTest {

    @Test
    public void testRedisCommand() {
        RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        RedisCommands<String, String> syncCommands = connection.sync();

        syncCommands.set("key", "Hello, Redis!");

        log.info("get {}", syncCommands.get("key"));

        connection.close();
        redisClient.shutdown();
    }
}
