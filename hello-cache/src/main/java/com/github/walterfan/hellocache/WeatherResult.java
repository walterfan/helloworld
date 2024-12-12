package com.github.walterfan.hellocache;


import lombok.Data;

import java.util.List;

/**
 * Created by yafan on 14/10/2017.
 */
@Data
public class WeatherResult {
    private String currentCity;
    private String pm25;

    private List<WeatherIndex> index;

    private List<WeatherData> weather_data;


}
