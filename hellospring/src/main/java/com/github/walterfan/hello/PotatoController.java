package com.github.walterfan.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yafan on 22/4/2018.
 */
@Controller
public class PotatoController {

    @RequestMapping("/")
    @ResponseBody
    public String hello() {
        return "hello world";
    }

}
