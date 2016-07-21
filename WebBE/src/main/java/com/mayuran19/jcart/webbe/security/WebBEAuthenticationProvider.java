package com.mayuran19.jcart.webbe.security;

import com.mayuran19.jcart.core.model.db.User;
import com.mayuran19.jcart.core.service.UserService;
import com.mayuran19.jcart.webbe.model.db.WebBEUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Created by mayuran on 18/7/16.
 */

@Component("webBEAuthenticationProvider")
public class WebBEAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebBEAuthenticationToken token = (WebBEAuthenticationToken) authentication;
        User user = userService.findUserByUsername((String) token.getPrincipal());
        WebBEUser webBEUser = new WebBEUser(user);

        return new WebBEAuthenticationToken(webBEUser.getUsername(), webBEUser.getPassword(), webBEUser.getAuthorities(), token.getMerchantId());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WebBEAuthenticationToken.class.equals(authentication);
    }
}
