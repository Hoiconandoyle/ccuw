package com.mailserver.service.filter;

import com.mailserver.model.mail.Mail;

import java.util.List;

public class ReceiverFilter implements Filter{
    private String receiver;
    public ReceiverFilter(String receiver){
        this.receiver = receiver;
    }

    @Override
    public List<Mail> filter(List<Mail> mails) {
        return mails.stream()
                .filter(mail -> {
                    for(String r : mail.getTo()){
                        if(r.toLowerCase().contains(receiver.toLowerCase())){
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }
}
