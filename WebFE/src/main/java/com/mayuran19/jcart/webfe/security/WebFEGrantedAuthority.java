package com.mayuran19.jcart.webfe.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by mayuran on 17/7/16.
 */
public class WebFEGrantedAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "USER";
    }
}
