package com.github.walterfan.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yafan on 4/4/2018.
 */
@Configuration
public class MainConfig
{
    @Bean
    public Potato getPotato()
    {
        Potato p = new Potato();
        p.setName("rest");
        return p;
    }
}