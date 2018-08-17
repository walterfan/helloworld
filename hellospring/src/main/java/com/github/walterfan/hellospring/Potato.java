package com.github.walterfan.hellospring;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.List;

@Data
@Slf4j
public class Potato {
    private String id;

    private String name;

    private int priority;

    private String description;

    private List<String> tags;

    private Instant deadline;

    private Instant createTime;

    @PostConstruct
    public void setup() {
        log.info("Potato setup");
    }

    @PreDestroy
    public void teardown() {
        log.info("Potato teardown");
    }
}
