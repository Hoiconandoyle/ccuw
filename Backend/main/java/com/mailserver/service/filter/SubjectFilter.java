package com.mailserver.service.filter;

import com.mailserver.model.mail.Mail;

import java.util.List;

public class SubjectFilter implements Filter{
    private String subject;
    public SubjectFilter(String subject){
        this.subject = subject;
    }
    @Override
    public List<Mail> filter(List<Mail> mails) {
        return mails.stream()
                .filter(mail -> mail.getSubject().toLowerCase().contains(subject.toLowerCase()))
                .toList();
    }
}
