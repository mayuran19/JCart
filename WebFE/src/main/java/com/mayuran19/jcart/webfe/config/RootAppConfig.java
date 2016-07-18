package com.mayuran19.jcart.webfe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mayuran on 16/7/16.
 * This class is used to configure whole application related settings. This class is registered
 * in RootAppInitializer
 */

@Configuration
@Import({WebFESecurityConfig.class})
public class RootAppConfig {

}
