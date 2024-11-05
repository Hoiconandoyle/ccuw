package com.mailserver.service.filter;

import com.mailserver.model.mail.Mail;

import java.util.List;

public class SenderFilter implements Filter{

    private String sender;
    public SenderFilter(String sender){
        this.sender = sender;
    }

    @Override
    public List<Mail> filter(List<Mail> mails) {
        return mails.stream()
                .filter(mail -> mail.getFrom().toLowerCase().contains(sender.toLowerCase()))
                .toList();
    }
}
