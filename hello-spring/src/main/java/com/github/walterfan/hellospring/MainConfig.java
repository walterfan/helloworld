package com.github.walterfan.hellospring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;


@Configuration
@Slf4j
public class MainConfig
{
    private AtomicLong idGeneerator = new AtomicLong(0);

    @Bean
    public Function<String, Potato> potatoFactory() {
        return name -> {
            Potato p = getPotato(name);
            p.setCreateTime(Instant.now());
            return p;
        };
    }

    @Bean
    @Scope("prototype")
    public Potato getPotato(String name)
    {
        Potato p = new Potato();
        p.setId(String.valueOf(idGeneerator.incrementAndGet()));
        p.setName(name);

        return p;
    }

    @Bean
    public MainApp mainApp()
    {
        return new MainApp();
    }

    @Bean
    public FileService fileSerivce()
    {
        return new FileService();
    }
}