package com.github.walterfan.hellospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by yafan on 22/4/2018.
 */
//the following annotation equals @Configuration, @EnableAutoConfiguration, @ComponentScan
@SpringBootApplication
public class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
