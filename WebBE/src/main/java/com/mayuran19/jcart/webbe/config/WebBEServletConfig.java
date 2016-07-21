package com.mayuran19.jcart.webbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by mayuran on 18/7/16.
 */
@Configuration
@ImportResource({})
@ComponentScan(basePackages = {
        "com.mayuran19.jcart.webbe.controller"
})
@EnableWebMvc
public class WebBEServletConfig extends WebMvcConfigurerAdapter{
    //View resolver used by controller to resolve the string returned by controller
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setOrder(1);

        return viewResolver;
    }
}
