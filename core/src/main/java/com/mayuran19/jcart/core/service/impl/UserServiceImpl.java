package com.mayuran19.jcart.core.service.impl;

import com.mayuran19.jcart.core.model.db.Group;
import com.mayuran19.jcart.core.model.db.User;
import com.mayuran19.jcart.core.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mayuran on 18/7/16.
 */

@Service("userService")
public class UserServiceImpl implements UserService {
    @Override
    public User findUserByUsername(String username) {
        // @TODO Replate with real implementation later
        if (username.equals("mayuran")) {
            User user = new User();
            user.setPassword("password");
            user.setUsername(username);
            user.setGroups(Stream.of("ROLE_USER", "ROLE_ADMIN").map((s) -> {
                Group group = new Group();
                group.setGroupName(s);
                return group;
            }).collect(Collectors.toList()));

            return user;
        }else{
            return null;
        }

    }
}
