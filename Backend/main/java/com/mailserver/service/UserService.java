package com.mailserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailserver.model.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    private static UserService instance = new UserService();
    private HashMap<String,User> users;
    private final ObjectMapper objectMapper = new ObjectMapper();
    String filePath = "src\\main\\java\\com\\mailserver\\database\\";

    private UserService() {
        users = new HashMap<>();
    }

    //Singleton instance to be used in the whole application
    public static UserService getInstance() {
        if(instance == null)
            instance = new UserService();
        return instance;
    }
    public User addUser(User user) {
        users.put(user.getEmail(),user);
        saveUsers(user.getEmail());
        return user;
    }
    public User getUserByEmail(String email) {
        return users.get(email);
    }
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void saveUsers(String email){
        try{
            String path = filePath + email.split("@")[0]+".json";
            objectMapper.findAndRegisterModules();
            objectMapper.writeValue(new File(path),this.getUserByEmail(email));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadUsers(){
        try{
            File[] files = new File("src\\main\\java\\com\\mailserver\\database")
                    .listFiles((dir, name) -> name.endsWith(".json"));

            objectMapper.findAndRegisterModules();
            if (files != null) {
                for (File file : files) {
                    User user = objectMapper.readValue(file, User.class);
                    addUser(user);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
