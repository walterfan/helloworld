package com.github.walterfan.hellocache;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yafan on 14/10/2017.
 */
@EnableAspectJAutoProxy
@ComponentScan
@Configuration
public class WeatherCacheConfig {//implements EnvironmentAware

    @Autowired
    private Environment environment;

    @Bean
    public WeatherCacheLoader weatherCacheLoader() {
        return new WeatherCacheLoader();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonConverter.setObjectMapper(new ObjectMapper());
                jsonConverter.setSupportedMediaTypes(ImmutableList.of(
                        new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET),
                        new MediaType("text", "javascript", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
            }
        }
        return restTemplate;
    }

    @Bean
    public String appToken() {
        return this.environment.getProperty("BAIDU_AK");
    }


    @Bean
    public LoadingCache<String, CityWeather> cityWeatherCache() {
        return CacheBuilder.newBuilder()
                .recordStats()
                .maximumSize(1000)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build(weatherCacheLoader());
    }

    @Bean
    public DurationTimerAspect durationTimerAspect() {
        return new DurationTimerAspect();
    }

    @Bean
    @Lazy
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }
}
