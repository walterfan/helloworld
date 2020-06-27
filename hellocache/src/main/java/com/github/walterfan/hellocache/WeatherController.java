package com.github.walterfan.hellocache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Created by yafan on 8/10/2017.
 */
@Slf4j
@Controller
public class WeatherController {
    @Autowired
    private WeatherService weatherService;
    // inject via application.properties
    @Value("${hello.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("cityName", "hefei");
        model.addAttribute("currentTime", Instant.now().toString());
        return "index";
    }



    @GetMapping("/weather")
    protected String queryWeather(Model model, HttpServletRequest req, HttpServletResponse resp) {


        String cityName = req.getParameter("cityName");
        model.addAttribute("cityName", cityName);
        Optional<CityWeather> cityWeather =  weatherService.getWeather(cityName);
        if(cityWeather.isPresent()) {
            log.info("weather: {}", cityWeather.get());
            model.addAttribute("cityWeather", cityWeather.get());
            model.addAttribute("weatherString", cityWeather.map(x->x.toString()).orElse(""));
        }
        return "index";
    }



}
