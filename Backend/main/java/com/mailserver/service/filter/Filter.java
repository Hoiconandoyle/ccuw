package com.mailserver.service.filter;

import com.mailserver.model.mail.Mail;

import java.util.List;

public interface Filter {
    List<Mail> filter(List<Mail> mails);
}
