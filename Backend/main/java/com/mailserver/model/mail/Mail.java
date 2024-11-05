package com.mailserver.model.mail;

import com.mailserver.model.Attachment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class Mail {

    private String id;
    private String from;
    private List<String> to;
    private String subject;
    private String body;
    private LocalDateTime dateTime;
    private Priority priority;
    private List<Attachment> attachments;

    public Mail(){
        this.to = new ArrayList<>();
    }
    public Mail(String from, List<String> to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Mail(MailBuilder mailBuilder) {
        this.id = mailBuilder.getId();
        this.from = mailBuilder.getFrom();
        this.to = mailBuilder.getTo();
        this.subject = mailBuilder.getSubject();
        this.body = mailBuilder.getBody();
        this.dateTime = mailBuilder.getDateTime();
        this.priority = mailBuilder.getPriority();
        this.attachments = mailBuilder.getAttachments();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(id, mail.id) && Objects.equals(from, mail.from)
                && Objects.equals(to, mail.to) && Objects.equals(subject, mail.subject)
                && Objects.equals(body, mail.body) && Objects.equals(dateTime, mail.dateTime)
                && priority == mail.priority && Objects.equals(attachments, mail.attachments);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, subject, body, dateTime, priority, attachments);
    }
}
