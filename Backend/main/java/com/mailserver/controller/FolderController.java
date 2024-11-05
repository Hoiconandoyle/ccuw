package com.mailserver.controller;

import com.mailserver.service.FolderService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FolderController {
    private FolderService folderService;
    public FolderController(FolderService folderService){
        this.folderService = folderService;
    }
    @PostMapping("/folder/{email}/{folderName}")
    public void createFolder(@PathVariable  String email, @PathVariable String folderName){
        folderService.createFolder(email,folderName);
    }
    @DeleteMapping("/folder/{email}/{folderName}")
    public void deleteFolder(@PathVariable  String email, @PathVariable String folderName){
        folderService.deleteFolder(email,folderName);

    }

    @PostMapping("/folder/{email}/{oldName}/{newName}")
    public void renameFolder(@PathVariable  String email, @PathVariable String oldName,@PathVariable String newName){
        folderService.renameFolder(email,oldName,newName);
    }
}
