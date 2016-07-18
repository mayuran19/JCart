package com.mayuran19.jcart.core.service;

import com.mayuran19.jcart.core.model.db.User;

/**
 * Created by mayuran on 18/7/16.
 */
public interface UserService {
    public User findUserByUsername(String username);
}
