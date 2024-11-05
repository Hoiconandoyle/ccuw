package com.mailserver.service.authentication;

import com.mailserver.service.UserService;

public class ValidPasswordHandler extends Handler {
    private UserService userService = UserService.getInstance();
    @Override
    public boolean handle(String email, String password) {
        if(!userService.getUserByEmail(email).getPassword().equals(password))
            return false;
        return checkNext(email, password);
    }
}
