package com.github.walterfan.hellometrics;

import com.codahale.metrics.MetricRegistry;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class ImageCacheLoader extends CacheLoader<String, Image> {



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String appToken;

    @Autowired
    private LoadingCache<String, Image> imageCache;

    @Autowired
    private MetricRegistry metricRegistry;

    //@DurationTimer(name="getCityWeather")
    public Image getCityWeather(String key) throws ExecutionException {
        return this.imageCache.get(key);
    }


    @Override
    public Image load(String city) throws Exception {

       /* String url = "http://api.map.baidu.com/telematics/v3/weather";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("location", city)
                .queryParam("output", "json")
                .queryParam("ak", appToken);

        ResponseEntity<CityWeather> resp = restTemplate.getForEntity(builder.toUriString(), CityWeather.class);

        logger.debug("response status: " + resp.getStatusCode());
        return resp.getBody();*/
       return null;
    }




}
