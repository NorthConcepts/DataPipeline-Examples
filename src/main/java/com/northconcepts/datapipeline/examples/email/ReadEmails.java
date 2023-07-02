package com.northconcepts.datapipeline.examples.email;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.email.EmailReader;
import com.northconcepts.datapipeline.email.MailStore;
import com.northconcepts.datapipeline.job.Job;

public class ReadEmails {

    public static void main(String[] args) {
        DataReader reader = new EmailReader(MailStore.POP3, "pop3.example.com", "user", "password");
        DataWriter writer = new StreamWriter(System.out);
        
        Job.run(reader, writer);
    }

}
