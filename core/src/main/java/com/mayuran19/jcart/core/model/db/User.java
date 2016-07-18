package com.mayuran19.jcart.core.model.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayuran on 18/7/16.
 */
public class User extends BaseDBModel{
    private String username;
    private String password;
    private List<Group> groups = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
