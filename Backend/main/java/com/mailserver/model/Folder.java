package com.mailserver.model;

import com.mailserver.model.mail.Mail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Folder {
    private String name;
    private List<Mail> mails;

    public Folder(String name) {
        this.name = name;
        this.mails = new ArrayList<>();
    }
    public Folder() {
        this.mails = new ArrayList<>();
    }
    public Folder(String name, List<Mail> mails) {
        this.name = name;
        this.mails = mails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }
}
