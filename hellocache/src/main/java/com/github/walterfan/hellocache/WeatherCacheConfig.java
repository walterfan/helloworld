package com.github.walterfan.hellocache;


import com.codahale.metrics.MetricRegistry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.GuavaCacheMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yafan on 14/10/2017.
 */
@EnableAspectJAutoProxy
@ComponentScan
@Configuration
@Slf4j
public class WeatherCacheConfig {//implements EnvironmentAware

    @Autowired
    private Environment environment;

    @Bean
    public WeatherCacheLoader weatherCacheLoader() {
        return new WeatherCacheLoader();
    }

    @Bean
    public RestTemplate restTemplate() {

        final RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean
    public String appToken() {
        return this.environment.getProperty("BAIDU_AK");
    }


    @Bean
    public LoadingCache<String, CityWeather> cityWeatherCache() {
        LoadingCache<String, CityWeather> cache = CacheBuilder.newBuilder()
                .recordStats()
                .maximumSize(1000)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build(weatherCacheLoader());

        recordCacheMetrics("cityWeatherCache", cache);

        recordCacheMeters("cityWeatherCache", cache);

        GuavaCacheMetrics.monitor(meterRegistry(), cache, "cityWeatherCache");

        return cache;
    }

    public void recordCacheMetrics(String cacheName, Cache cache) {

        MetricRegistry metricRegistry = metricRegistry();

        metricRegistry.gauge(makeMetricsKeyName(cacheName, "hitCount"), () -> () -> cache.stats().hitCount());
        metricRegistry.gauge(makeMetricsKeyName(cacheName, "hitRate"), () -> () -> cache.stats().hitRate());

        metricRegistry.gauge(makeMetricsKeyName(cacheName, "missCount"), () -> () -> cache.stats().missCount());
        metricRegistry.gauge(makeMetricsKeyName(cacheName, "missRate"), () -> () -> cache.stats().missRate());

        metricRegistry.gauge(makeMetricsKeyName(cacheName, "requestCount"), () -> () -> cache.stats().requestCount());

        metricRegistry.gauge(makeMetricsKeyName(cacheName, "loadCount"), () -> () -> cache.stats().loadCount());
        metricRegistry.gauge(makeMetricsKeyName(cacheName, "loadSuccessCount"), () -> () -> cache.stats().loadSuccessCount());
        metricRegistry.gauge(makeMetricsKeyName(cacheName, "loadExceptionCount"), () -> () -> cache.stats().loadExceptionCount());
    }

    public void recordCacheMeters(String cacheName, Cache cache) {
        MeterRegistry meterRegistry = meterRegistry();
        Gauge.builder(makeMetricsKeyName(cacheName, "hitCount"), () -> cache.stats().hitCount()).register(meterRegistry);
        Gauge.builder(makeMetricsKeyName(cacheName, "hitRate"), () -> cache.stats().hitRate()).register(meterRegistry);

        Gauge.builder(makeMetricsKeyName(cacheName, "missCount"), () -> cache.stats().missCount()).register(meterRegistry);
        Gauge.builder(makeMetricsKeyName(cacheName, "missRate"), () -> cache.stats().missRate()).register(meterRegistry);

        Gauge.builder(makeMetricsKeyName(cacheName, "requestCount"), () -> cache.stats().requestCount()).register(meterRegistry);

        Gauge.builder(makeMetricsKeyName(cacheName, "loadCount"), () -> cache.stats().loadCount()).register(meterRegistry);
        Gauge.builder(makeMetricsKeyName(cacheName, "loadSuccessCount"), () -> cache.stats().loadSuccessCount()).register(meterRegistry);
        Gauge.builder(makeMetricsKeyName(cacheName, "loadExceptionCount"), () -> cache.stats().loadExceptionCount()).register(meterRegistry);

    }

    public String makeMetricsKeyName(String cacheName, String keyName) {
        String metricKey = MetricRegistry.name("cache", cacheName, keyName);
        log.info("metric key generated for cache: {}", metricKey);
        return metricKey;
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

    @Bean
    public MeterRegistry meterRegistry() {
        CompositeMeterRegistry compositeRegistry = new CompositeMeterRegistry();

        SimpleMeterRegistry simpleMeter = new SimpleMeterRegistry();
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        compositeRegistry.add(simpleMeter);
        compositeRegistry.add(prometheusMeterRegistry);
        return compositeRegistry;
    }
}
