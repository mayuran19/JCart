package com.mayuran19.jcart.webbe.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by mayuran on 18/7/16.
 */
public class WebBEAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String merchantId;

    public WebBEAuthenticationToken(Object principal, Object credentials, String merchantId) {
        super(principal, credentials);
        this.merchantId = merchantId;
    }

    public WebBEAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String merchantId) {
        super(principal, credentials, authorities);
        this.merchantId = merchantId;
    }

    public String getMerchantId() {
        return merchantId;
    }
}
