package com.northconcepts.datapipeline.mailchimp;

import com.northconcepts.datapipeline.mailchimp.list.ListMember;
import com.northconcepts.datapipeline.mailchimp.list.ListMemberStatus;
import com.northconcepts.datapipeline.mailchimp.list.ListMembers;

public class ReadMailChimpContactsUsingClientAPI {
    private static final String apiKey = "api-key";
    private static final String listId = "list-id";

    private static final MailChimpClient client = MailChimpClient.Proxy.get(apiKey);
    
    public static void main(String[] args) {
        ListMembers listMembers = client.getListMembersObject(listId, ListMemberStatus.subscribed, 0, 5);
        
        System.out.println("Total contacts :"+listMembers.getMembers().size());
        for (ListMember listMember: listMembers.getMembers()) {
            System.out.println("---------------------------");
            System.out.println("Id :"+listMember.getId());
            System.out.println("Email :"+listMember.getEmailAddress());
            System.out.println("Status :"+listMember.getStatus());
        }
    }
    
}
