package com.mailserver.controller;

import com.mailserver.model.User;
import com.mailserver.service.UserService;
import com.mailserver.service.authentication.Handler;
import com.mailserver.service.authentication.UserExistsHandler;
import com.mailserver.service.authentication.ValidPasswordHandler;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private  UserService userService = UserService.getInstance();


    @PostMapping("/user/register")
    public User register(@Valid @RequestBody User user){
        //check if user already exists
        if(userService.getUserByEmail(user.getEmail()) != null)
            throw new RuntimeException("User already exists!");
        return userService.addUser(user);
    }

    @PostMapping("/user/login")
    public boolean logIn(@RequestBody User user){
        Handler handler = new UserExistsHandler();
        handler.setNextHandler(new ValidPasswordHandler());
        return handler.handle(user.getEmail(),user.getPassword());
    }

    @GetMapping("/user")
    public List<User>getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/user/save")
    public void saveUsers(){
        userService.saveUsers("mohamed@gmail.com");
    }

    @GetMapping("/user/load")
    public void loadUsers(){
        userService.loadUsers();
    }

}
