package com.mailserver.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Contact implements Comparable<Contact> {
    private String name;
    private List<String> emailAddresses;

    public Contact() {
    }

    public Contact(String name, String emailAddress) {
        this.name = name;
        this.emailAddresses = new ArrayList<>();
        emailAddresses.add(emailAddress);
    }

    public Contact(String name, List<String> emailAddresses) {
        this.name = name;
        this.emailAddresses = emailAddresses;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getEmailAddresses() {
        return this.emailAddresses;
    }

    @Override
    public int compareTo(Contact other) {
        return this.name.compareTo(other.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(int email_index, String newEmailAddress) {
        emailAddresses.set(email_index, newEmailAddress);
    }

    public void addEmailAddress(String newEmailAddress) {
        emailAddresses.add(newEmailAddress);
    }

    public void removeEmailAddress(String removedEmailAddress) {
        emailAddresses.remove(removedEmailAddress);
    }
}