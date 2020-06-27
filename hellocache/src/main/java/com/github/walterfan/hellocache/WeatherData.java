package com.github.walterfan.hellocache;

import lombok.Data;

/**
 * Created by yafan on 14/10/2017.
 */
@Data
public class WeatherData {

    private String date;
    private String dayPictureUrl;
    private String nightPictureUrl;
    private String weather;
    private String wind;
    private String temperature;


}

