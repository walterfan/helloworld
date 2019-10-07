package com.github.walterfan.helloworld.taskservice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TaskController {

    @Value("${database.name}")
    private String dbName;

    @GetMapping("/database/name")
    public String getDatabaseName() {
        log.info("getDatabaseNameh");
        return this.dbName;
    }
}