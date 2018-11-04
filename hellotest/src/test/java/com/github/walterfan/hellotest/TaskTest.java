package com.github.walterfan.hellotest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.org.apache.xml.internal.serializer.SerializerBase;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;


import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * Created by yafan on 20/11/2017.
 */
@Slf4j
public class TaskTest {

    private String getTaskNames(List<Task> meetings) {
        return meetings.stream()
                .map(m -> m.getName())
                .collect(Collectors.joining(","));
    }

    @Test
    public void toJsonTest() throws JsonProcessingException {
        Task task = new Task();
        task.setDeadline(Instant.now().plus(25, ChronoUnit.MINUTES) );
        task.setDuration(Duration.ofMinutes(25));
        task.setId(UUID.randomUUID());
        task.setName("learnSpringTest");


        String jsonStr = task.toJson();
        log.info(jsonStr);

        String jsonPath = "$['duration']";
        DocumentContext jsonContext = JsonPath.parse(jsonStr);
        String duration = jsonContext.read(jsonPath);
        assertTrue("PT25M".equals(duration));
    }

    @Test
    public void fromJsonTest() throws IOException {
        String json = "{\"id\":\"68e93ae0-e372-43ed-93bc-654b866a64af\"," +
                "\"name\":\"learnSpringTest\"," +
                "\"duration\":\"PT30M\"," +
                "\"deadline\":\"2017-11-20T18:00:00.000Z\"}";


        Task task = Task.fromJson(json);
        assertEquals(30, task.getDuration().toMinutes());
    }

    @Test
    public void testTimestamp() {
        long duration = System.currentTimeMillis()/1000 - 1516398923L;

        System.out.println(duration/86400);

        Task task = new Task();
        task.setName("task1");

        Task task2 = new Task();
        task2.setName("task2");

        List<Task> list = Arrays.asList(task, task2);

        String taskNames = getTaskNames(list);
        System.out.println(taskNames);
    }
}
