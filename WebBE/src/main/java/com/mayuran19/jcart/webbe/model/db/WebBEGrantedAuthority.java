package com.mayuran19.jcart.webbe.model.db;

import com.mayuran19.jcart.core.model.db.Group;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by mayuran on 18/7/16.
 */
public class WebBEGrantedAuthority extends Group implements GrantedAuthority {
    private Group group;
    public WebBEGrantedAuthority(Group group) {
        this.group = group;
    }

    @Override
    public String getAuthority() {
        return group.getGroupName();
    }
}
