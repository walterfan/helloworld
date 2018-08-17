package com.github.walterfan.hellospring;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yafan on 4/5/2018.
 */
@Controller
public class PotatoWebController {
    @Autowired
    private PotatoService potatoService;

    @RequestMapping("/potatoes")
    public String welcome(@RequestParam(defaultValue = "Walter") String name,  Model model) {
        model.addAttribute("message", name + ",  welcome to potato workshop at " + new Date());
        return "welcome";
    }

    @RequestMapping(value = "/potatoes/search", method = RequestMethod.POST)
    public String search(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String keywords = request.getParameter("keywords");
        if(StringUtils.isBlank(keywords)) {
            redirectAttributes.addFlashAttribute("error" , "please input keywords");
            return "redirect:/potatoes";
        }

        redirectAttributes.addAttribute("keywords", keywords);

        return "redirect:/potatoes/result";
    }

    @RequestMapping(value = "/potatoes/result", method = RequestMethod.GET)
    public String queryPotatoes(@RequestParam String keywords, Model model) {
        List<Potato> potatoList = potatoService.search(Arrays.asList(keywords.split(",")));
        model.addAttribute("potatoes", potatoList);
        return "welcome";
    }
}
