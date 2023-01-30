package com.northconcepts.datapipeline.mailchimp;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.LimitReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.mailchimp.list.ListMemberStatus;

public class ReadMailChimpContacts {

    private static final String apiKey = "api-key";
    private static final String listId = "list-id";
    
    public static void main(String[] args) {
        DataReader reader = new MailChimpListMemberReader(apiKey, listId)
                .setStatus(ListMemberStatus.subscribed);
        
        reader = new LimitReader(reader, 5);
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }
    
}
