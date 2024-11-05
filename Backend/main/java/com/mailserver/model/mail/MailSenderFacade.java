package com.mailserver.model.mail;

import com.mailserver.model.Attachment;
import com.mailserver.model.User;
import com.mailserver.service.AttachmentService;
import com.mailserver.service.DraftService;
import com.mailserver.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MailSenderFacade {
    private UserService userService = UserService.getInstance();
    private DraftService draftService;
    private AttachmentService attachmentService;

    public MailSenderFacade(DraftService draftService,AttachmentService attachmentService) {
        this.draftService = draftService;
        this.attachmentService = attachmentService;
    }
    public  Mail sendMail(Mail mail,boolean wasDraft,List<String> attachmentIds){

        List<Attachment> attachments = new ArrayList<>();
        if(wasDraft){
            List<Mail> drafts= userService.getUserByEmail(mail.getFrom()).getDrafts();
            for (Mail draft : drafts){
                if(Objects.equals(draft.getId(), mail.getId())) {
                    attachments = draft.getAttachments();
                    break;
                }
            }
            draftService.deleteDraft(mail.getFrom(),mail.getId());
        }

        mail.setDateTime(LocalDateTime.now().withNano(0));
        mail.setId(Long.toString(System.currentTimeMillis()));

        for (String attachmentId : attachmentIds) {
            if(attachmentService.get(attachmentId) == null)continue;
            attachments.add(attachmentService.get(attachmentId));
            attachmentService.remove(attachmentId);
        }
        mail.setAttachments(attachments);

        String from = mail.getFrom();
        List<String> to = mail.getTo();


        for(String email:to){
            User receiver = userService.getUserByEmail(email);
            if(receiver != null){
                receiver.getInbox().add(mail);
            }
        }

        User sender = userService.getUserByEmail(from);
        if(sender != null){
            sender.getSent().add(mail);
        }

        userService.saveUsers(from);
         for(String email:to){
            if(userService.getUserByEmail(email) != null)
                userService.saveUsers(email);
        }
        return mail;
    }
}
