package com.mayuran19.jcart.webbe.security;

import com.mayuran19.jcart.core.service.UserService;
import com.mayuran19.jcart.webbe.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mayuran on 18/7/16.
 */

public class WebBEAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = super.obtainUsername(request);
        String password = super.obtainPassword(request);
        String merchantId = request.getParameter(Constants.RequestParameter.PARAM_MERCHANT_ID);

        WebBEAuthenticationToken token = new WebBEAuthenticationToken(username, password, merchantId);
        super.setDetails(request, token);

        return this.getAuthenticationManager().authenticate(token);
    }
}
