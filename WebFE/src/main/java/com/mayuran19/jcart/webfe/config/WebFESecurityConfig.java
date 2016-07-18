package com.mayuran19.jcart.webfe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by mayuran on 16/7/16.
 * This class is used to configure all the web related security configurations.
 * The urls starts with /*
 */

@Configuration
@ComponentScan(basePackages = {"com.mayuran19.groupExpense.web.security"})
@EnableWebSecurity
@Order(1)
public class WebFESecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //allow anonymous resource request
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources/**").permitAll()

                //all other request need to be authenticated
                .antMatchers("/web/**").access("hasRole('USER')")
                .and().formLogin().and().exceptionHandling().accessDeniedPage("/accessDenied");
    }
}
