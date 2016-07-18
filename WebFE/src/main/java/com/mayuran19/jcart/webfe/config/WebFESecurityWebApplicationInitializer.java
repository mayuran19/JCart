package com.mayuran19.jcart.webfe.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by mayuran on 16/7/16.
 * This will create a filter with name "springSecurityFilterChain" that is of the type "FilterChainProxy"
 * and add it as the first filter to the application filter chain
 */
public class WebFESecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

}
