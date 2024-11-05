package com.mailserver.service.filter;

import com.mailserver.model.mail.Mail;

import java.util.List;

public class BodyFilter implements Filter{

    private String body;
    public BodyFilter(String body){
        this.body = body;
    }
    @Override
    public List<Mail> filter(List<Mail> mails) {
        return mails.stream()
                .filter(mail -> mail.getBody().toLowerCase().contains(body.toLowerCase()))
                .toList();
    }
}
