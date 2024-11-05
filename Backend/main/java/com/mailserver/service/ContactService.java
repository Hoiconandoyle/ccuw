package com.mailserver.service;

import com.mailserver.model.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ContactService {
    // Assuming that the contact is already a user so no need to have a collection
    // for contacts
    private UserService userService = UserService.getInstance();

    private boolean checkContactExistence(String contactEmailAddress) {
        if (userService.getUserByEmail(contactEmailAddress) == null) {
            return false;
        }
        return true;
    }

    private boolean checkContactSimilarity(String userEmailAddress, String newContactEmailAddress) {
        List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();
        for (Contact contact : contacts) {
            List<String> emails = contact.getEmailAddresses();
            for (String email : emails) {
                if (email.equalsIgnoreCase(newContactEmailAddress)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Contact> getContacts(String userEmailAddress) {
        return userService.getUserByEmail(userEmailAddress).getContacts();
    }

    public String createContact(String userEmailAddress, String contactName, String contactEmailAddress) {
        if (checkContactExistence(contactEmailAddress)
                && checkContactSimilarity(userEmailAddress, contactEmailAddress)) {
            Contact contact = new Contact(contactName, contactEmailAddress);
            userService.getUserByEmail(userEmailAddress).getContacts().add(contact);
            userService.saveUsers(userEmailAddress);
            return "Contact added successfully";
        } else if (!checkContactExistence(contactEmailAddress)) {
            return "No Such User Found";
        }
        return "Another Contact Has This Email";
    }

    public String renameContact(String userEmailAddress, String newContactName, String contactEmailAddress) {
        List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();

        for (Contact contact : contacts) {
            List<String> emails = contact.getEmailAddresses();
            for (String email : emails) {
                if (email.equalsIgnoreCase(contactEmailAddress)) {
                    contact.setName(newContactName);
                    break;
                }
            }
        }
        userService.saveUsers(userEmailAddress);
        return "Contact edited successfully";
    }

    public String resetContact(String userEmailAddress, String contactEmailAddress, String newContactEmailAddress) {

        if (checkContactExistence(newContactEmailAddress)
                && checkContactSimilarity(userEmailAddress, newContactEmailAddress)) {
            List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();
            for (Contact contact : contacts) {
                int email_number = 0;
                List<String> emails = contact.getEmailAddresses();
                for (String email : emails) {
                    if (email.equalsIgnoreCase(contactEmailAddress)) {
                        contact.setEmailAddress(email_number, newContactEmailAddress);
                        userService.saveUsers(userEmailAddress);
                        return "Contact email successfully reset";
                    }
                    email_number++;
                }
            }
        } else if (!checkContactExistence(newContactEmailAddress)) {
            return "No Such User Found";
        }
        return "Another Contact Has This Email";
    }

    public String addEmail(String userEmailAddress, String contactEmailAddress, String newContactEmailAddress) {
        if (checkContactExistence(newContactEmailAddress)
                && checkContactSimilarity(userEmailAddress, newContactEmailAddress)) {
            List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();
            for (Contact contact : contacts) {
                List<String> emails = contact.getEmailAddresses();
                for (String email : emails) {
                    if(email.equalsIgnoreCase(contactEmailAddress)) {
                        contact.addEmailAddress(newContactEmailAddress);
                        userService.saveUsers(userEmailAddress);
                        return "Contact email successfully added";
                    }
                }
            }
        } else if (!checkContactExistence(newContactEmailAddress)) {
            return "No Such User Found";
        }
        return "Another Contact Has This Email";
    }

    public String removeEmail(String userEmailAddress, String contactEmailAddress, String removedContactEmailAddress) {
        List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();
        for (Contact contact : contacts) {
            List<String> emails = contact.getEmailAddresses();
            for (String email : emails) {
                if(email.equalsIgnoreCase(contactEmailAddress)){
                    if(emails.size() == 1) {
                        contacts.remove(contact);
                        userService.saveUsers(userEmailAddress);
                        return "Contact Deleted";
                    }
                    contact.removeEmailAddress(removedContactEmailAddress);
                    userService.saveUsers(userEmailAddress);
                    return "Contact email successfully removed";
                }
            }
        }
        return "No Such Contact Found";
    }

    public void deleteContact(String userEmailAddress, String contactEmailAddress) {
        List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();
        contacts.removeIf(contact -> contact.getEmailAddresses().contains(contactEmailAddress));
        userService.saveUsers(userEmailAddress);
    }

    public List<Contact> sortContacts(String userEmailAddress, String order) {
        List<Contact> contacts = userService.getUserByEmail(userEmailAddress).getContacts();
        Comparator<Contact> compareByName = Comparator.comparing(Contact::getName);
        if (order.equalsIgnoreCase("ascending")) {
            contacts.sort(compareByName);
        } else if (order.equalsIgnoreCase("descending")) {
            contacts.sort(compareByName.reversed());
        }
        return contacts;
    }

    public List<Contact> searchContacts(String userEmailAddress, String contactName) {
        return userService.getUserByEmail(userEmailAddress).getContacts()
                .stream().filter(contact -> contact.getName().contains(contactName)).toList();
    }
}