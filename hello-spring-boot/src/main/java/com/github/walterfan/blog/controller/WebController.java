package com.github.walterfan.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yafan on 13/12/2017.
 */
@Controller
public class WebController {

    @RequestMapping(value = "/{page}")
    public String index(@PathVariable String page) {
        return page;
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
