package com.mailserver.service;

import com.mailserver.model.Folder;
import org.springframework.stereotype.Service;

@Service
public class FolderService {
    private UserService userService = UserService.getInstance();

    public void createFolder(String email, String folderName) {
        userService.getUserByEmail(email).getFolders().add(new Folder(folderName));
        userService.saveUsers(email);
    }

    public void deleteFolder(String email, String folderName) {
        userService.getUserByEmail(email).getFolders()
                .removeIf(folder -> folder.getName().equalsIgnoreCase(folderName));
        userService.saveUsers(email);
    }

    public Folder getFolder(String email, String folderName) {
        return userService.getUserByEmail(email).getFolders().stream()
                .filter(folder -> folder.getName().equalsIgnoreCase(folderName))
                .findFirst().orElse(null);
    }

    public void renameFolder(String email, String oldName, String newName) {
        Folder folder = getFolder(email, oldName);
        folder.setName(newName);
        userService.saveUsers(email);
    }

    public boolean folderExists(String email, String toFolder) {
        return userService.getUserByEmail(email).getFolders().stream()
                .anyMatch(folder -> folder.getName().equalsIgnoreCase(toFolder))
                || toFolder.equalsIgnoreCase("inbox") || toFolder.equalsIgnoreCase("sent")
                || toFolder.equalsIgnoreCase("trash")  || toFolder.equalsIgnoreCase("draft");
    }
}
