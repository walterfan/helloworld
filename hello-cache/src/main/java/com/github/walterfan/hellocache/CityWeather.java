package com.github.walterfan.hellocache;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by yafan on 14/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CityWeather  {
    private int error;

    private String status;

    private Date date;

    private List<WeatherResult> results;

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}


