package com.mayuran19.jcart.webbe.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by mayuran on 16/7/16.
 * This will create a filter with name "springSecurityFilterChain" that is of the type "FilterChainProxy"
 * and add it as the first filter to the application filter chain
 * This is to bridge the web.xml filter with applicationContext filter bean
 */
public class WebBESecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

}
