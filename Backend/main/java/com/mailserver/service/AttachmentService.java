package com.mailserver.service;

import com.mailserver.model.Attachment;
import com.mailserver.model.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class AttachmentService {
    private final Map<String, Attachment> db = new HashMap<>();
    @Autowired
    private MailService mailService;

    public Collection<Attachment> get() {
        return db.values();
    }

    public Attachment get(String id) {
        return db.get(id);
    }

    public Attachment remove(String id) {
        return db.remove(id);
    }

    public Attachment upload(String fileName, String contentType, byte[] data) {
        String id = UUID.randomUUID().toString();
        Attachment attachment = new Attachment(id, fileName, contentType, data);
        db.put(attachment.getId(), attachment);
        return attachment;
    }
    private ResponseEntity<byte[]> downloadFile(Attachment attachment) {
        if (attachment == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        byte[] data = attachment.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(attachment.getContentType()));
        ContentDisposition build = ContentDisposition
                .builder("attachment")
                .filename(attachment.getFileName())
                .build();
        headers.setContentDisposition(build);
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
    public ResponseEntity<byte[]> download(String id) {
        Attachment attachment = db.get(id);
        return downloadFile(attachment);
    }
    public ResponseEntity<byte[]> downloadFromMail(String email,String folderName ,String mailId,String attachmentId) {
        List<Mail> mails = mailService.getMailsByFolderName(email, folderName);
        Attachment attachment = null;
        for (Mail mail : mails) {
            if (Objects.equals(mail.getId(), mailId)) {
                List<Attachment> attachments = mail.getAttachments();
                for (Attachment value : attachments) {
                    if (Objects.equals(value.getId(), attachmentId)) {
                        attachment = value;
                    }
                }
            }
        }
        return downloadFile(attachment);
    }
    public void deleteAttachment(String email, String mailId, String attachmentId) {
        List<Mail> mails = mailService.getMailsByFolderName(email, "draft");
        for (Mail mail : mails) {
            if (Objects.equals(mail.getId(), mailId)) {
                List<Attachment> attachments = mail.getAttachments();
                for (Attachment value : attachments) {
                    if (Objects.equals(value.getId(), attachmentId)) {
                        attachments.remove(value);
                    }
                }
            }
        }
    }
}