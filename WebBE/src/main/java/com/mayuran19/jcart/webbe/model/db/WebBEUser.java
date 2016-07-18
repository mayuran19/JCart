package com.mayuran19.jcart.webbe.model.db;

import com.mayuran19.jcart.core.model.db.Group;
import com.mayuran19.jcart.core.model.db.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mayuran on 18/7/16.
 */
public class WebBEUser extends User implements UserDetails {
    private User user;
    public WebBEUser(User user) {
        this.user = user;
    }

    //properties belongs to the Spring Security WebBEUser
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getGroups().stream().map((g) -> {
            WebBEGrantedAuthority authority = new WebBEGrantedAuthority(g);
            return authority;
        }).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
