package com.github.walterfan.hellometrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.List;

/**
 * @Author: Walter Fan
 * @Date: 19/7/2020, Sun
 **/
@Controller
@Slf4j
public class WebController {

    @Autowired
    private TomatoService tomatoService;

    @RequestMapping("/")
    public String index(Model model) {
        return listTomatoes(model);
    }


    @GetMapping(path = {"/tomatoes"})
    public String listTomatoes(Model model) {

        List<TomatoDTO> tomatoes = tomatoService.list(null, 0, 20);
        model.addAttribute("tomatoes",tomatoes);
        model.addAttribute("tomato", new TomatoDTO());
        log.info("listTomatoes: {}", tomatoes);
        return "index";
    }

    @PostMapping(path = {"/tomatoes"})
    public String createTomatoe(Model model, @ModelAttribute TomatoDTO tomato) {
        log.info("createTomatoe: {}", tomato);
        TomatoDTO tomatoDTO = tomatoService.create(tomato);
        model.addAttribute("tomato", tomatoDTO);
        return listTomatoes(model);

    }

}
