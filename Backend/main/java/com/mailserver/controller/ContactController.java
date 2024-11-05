package com.mailserver.controller;

import com.mailserver.model.Contact;
import com.mailserver.service.ContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {
    private ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // Create contact
    @PostMapping("/contact/create/{userEmailAddress}/{contactName}/{contactEmailAddress}")
    public String createContact(@PathVariable String userEmailAddress, @PathVariable String contactName,
            @PathVariable String contactEmailAddress) {
        return contactService.createContact(userEmailAddress, contactName, contactEmailAddress);
    }

    // edit contact
    @PostMapping("/contact/rename/{userEmailAddress}/{contactName}/{contactEmailAddress}")
    public String renameContact(@PathVariable String userEmailAddress, @PathVariable String contactName,
            @PathVariable String contactEmailAddress) {
        return contactService.renameContact(userEmailAddress, contactName, contactEmailAddress);
    }

    @PostMapping("/contact/reset/{userEmailAddress}/{contactEmailAddress}/{newContactEmailAddress}")
    public String resetContact(@PathVariable String userEmailAddress, @PathVariable String contactEmailAddress,
            @PathVariable String newContactEmailAddress) {
        return contactService.resetContact(userEmailAddress, contactEmailAddress, newContactEmailAddress);
    }

    @PostMapping("/contact/add/{userEmailAddress}/{contactEmailAddress}/{newContactEmailAddress}")
    public String addEmail(@PathVariable String userEmailAddress, @PathVariable String contactEmailAddress,
            @PathVariable String newContactEmailAddress) {
        return contactService.addEmail(userEmailAddress, contactEmailAddress, newContactEmailAddress);
    }

    @DeleteMapping("/contact/remove/{userEmailAddress}/{contactEmailAddress}/{removedContactEmailAddress}")
    public String removeEmail(@PathVariable String userEmailAddress, @PathVariable String contactEmailAddress,
            @PathVariable String removedContactEmailAddress) {
        return contactService.removeEmail(userEmailAddress, contactEmailAddress, removedContactEmailAddress);
    }

    @DeleteMapping("/contact/delete/{userEmailAddress}/{contactEmailAddress}")
    public void deleteContact(@PathVariable String userEmailAddress, @PathVariable String contactEmailAddress) {
        contactService.deleteContact(userEmailAddress, contactEmailAddress);
    }

    @GetMapping("/contact/sort/{userEmailAddress}/{order}")
    public List<Contact> sortContacts(@PathVariable String userEmailAddress, @PathVariable String order) {
        return contactService.sortContacts(userEmailAddress, order);
    }

    @GetMapping("/contact/search/{userEmailAddress}/{contactName}")
    public List<Contact> searchContacts(@PathVariable String userEmailAddress, @PathVariable String contactName) {
        return contactService.searchContacts(userEmailAddress, contactName);
    }

    @GetMapping("/contact/get/{userEmailAddress}")
    public List<Contact> getContacts(@PathVariable String userEmailAddress) {
        return contactService.getContacts(userEmailAddress);
    }
}