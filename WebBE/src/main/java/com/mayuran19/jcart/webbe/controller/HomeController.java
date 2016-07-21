package com.mayuran19.jcart.webbe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by mayuran on 18/7/16.
 */
@Controller(value = "homeController")
public class HomeController {
    @RequestMapping(value = "home")
    public String home(){
        return "home";
    }
}
