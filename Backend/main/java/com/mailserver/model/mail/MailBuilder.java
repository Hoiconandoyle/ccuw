package com.mailserver.model.mail;

import com.mailserver.model.Attachment;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

public class MailBuilder {
    private String id;
    private String from;
    private List<String> to;
    private String subject;
    private String body;
    private LocalDateTime dateTime;
    private Priority priority;
    private List<Attachment> attachments;

    public MailBuilder(){
    }
    public MailBuilder setId(String id) {
        this.id = id;
        return this;
    }
    public MailBuilder setFrom(String from) {
        this.from = from;
        return this;
    }
    public MailBuilder setTo(List<String> to) {
        this.to = to;
        return this;
    }
    public MailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }
    public MailBuilder setBody(String body) {
        this.body = body;
        return this;
    }
    public MailBuilder setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }
    public MailBuilder setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }
    public MailBuilder setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }
    public Mail build(){
        return new Mail(this);
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
