package com.mailserver.service.filter;

import com.mailserver.model.mail.Mail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrCriteria implements Filter{
    private List<Filter> filters;
    public OrCriteria(){
        filters = new ArrayList<>();
    }
    public void addFilter(Filter filter){
        filters.add(filter);
    }

    @Override
    public List<Mail> filter(List<Mail> mails) {

        List<Mail> filteredMails = new ArrayList<>();

        // Apply all filters to mails list
        for(Filter filter:filters){
            filteredMails.addAll(filter.filter(mails));
        }

        // Remove duplicates
        Set<Mail> uniqueFilteredMails = new HashSet<>(filteredMails);
        return new ArrayList<>(uniqueFilteredMails);
    }
}
