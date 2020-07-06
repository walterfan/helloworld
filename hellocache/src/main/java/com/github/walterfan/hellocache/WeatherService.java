package com.github.walterfan.hellocache;

import com.codahale.metrics.MetricRegistry;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Walter Fan
 * @Date: 27/6/2020, Sat
 **/
@Slf4j
@Service
public class WeatherService {
    @Autowired
    private LoadingCache<String, CityWeather> cityWeatherCache;

    @Autowired
    private MetricRegistry metricRegistry;


    public Optional<CityWeather> getWeather(String city) {
        try {
            return Optional.ofNullable(cityWeatherCache.get(city));
        } catch (ExecutionException e) {
            log.error("getWeather error", e);
            return Optional.empty();
        }
    }
}
