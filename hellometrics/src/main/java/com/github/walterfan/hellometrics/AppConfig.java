package com.github.walterfan.hellometrics;

import com.google.common.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class AppConfig {

    @Bean
    public CacheLoader<String, Image> cacheLoader() {
        return new ImageCacheLoader();
    }
}
