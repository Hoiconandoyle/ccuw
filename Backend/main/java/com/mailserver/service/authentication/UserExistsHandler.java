package com.mailserver.service.authentication;

import com.mailserver.service.UserService;

public class UserExistsHandler extends Handler {
    private UserService userService = UserService.getInstance();
    @Override
    public boolean handle(String email, String password) {
        if(userService.getUserByEmail(email) == null)
            return false;
        return checkNext(email, password);
    }
}
