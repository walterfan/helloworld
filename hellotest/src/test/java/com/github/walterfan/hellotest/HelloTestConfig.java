package com.github.walterfan.hellotest;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yafan on 20/11/2017.
 */
@Configuration
public class HelloTestConfig {

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class, Mockito.withSettings().defaultAnswer(Mockito.RETURNS_SMART_NULLS));
    }
}
