package com.github.walterfan.hellonetty;

import com.github.walterfan.hellonetty.netty.DiscardServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 */
@Slf4j
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public DiscardServer discardServer() {
        return new DiscardServer("localhost", 12345);
    }
}