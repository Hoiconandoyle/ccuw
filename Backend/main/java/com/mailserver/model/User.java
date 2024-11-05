package com.mailserver.model;

import com.mailserver.model.mail.Mail;
import com.mailserver.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class User implements Serializable {
    @NotBlank(message = "Username cannot be empty")
    private String userName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+com$", message = "Invalid email format.")
    private String email;

    private String password;
    private List<Mail> sent;
    private List<Mail> inbox;
    private List<Contact> contacts;
    private List<Mail> trash;
    private List<Folder> folders;
    private List<Mail> drafts;

    public User() {
        this.sent = new ArrayList<>();
        this.inbox = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.trash = new ArrayList<>();
        this.folders = new ArrayList<>();
        this.drafts = new ArrayList<>();
    }

    public User(String userName, String email, List<Mail> sent, List<Mail> inbox, List<Contact> contacts) {
        this.userName = userName;
        this.email = email;
        this.sent = sent;
        this.inbox = inbox;
        this.contacts = contacts;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public List<Mail> getSent() {
        return sent;
    }

    public void setSent(List<Mail> sent) {
        this.sent = sent;
    }

    public List<Mail> getInbox() {
        return inbox;
    }

    public void setInbox(List<Mail> inbox) {
        this.inbox = inbox;
    }

    public List<Contact> getContacts() {

        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Mail> getTrash() {
        return trash;
    }

    public void setTrash(List<Mail> trash) {
        this.trash = trash;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<Mail> getDrafts() {
        return drafts;
    }

    public void setDrafts(List<Mail> drafts) {
        this.drafts = drafts;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sent=" + sent +
                ", inbox=" + inbox +
                '}';
    }
}
