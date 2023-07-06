package com.northconcepts.datapipeline.examples.mongodb;

import java.io.OutputStreamWriter;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.mongodb.MongoReader;

public class ReadFromMongoDb {
    
    public static void main(String[] args) {
        String database = "datapipeline";
        String collection = "clients";

        DataReader reader = new MongoReader(database, collection);
        DataWriter writer = new CSVWriter(new OutputStreamWriter(System.out));
        
        Job.run(reader, writer);
    }
    /* CSV output to console
        Account,LastName,FirstName,Balance,CreditLimit,AccountCreated,Rating
        101,Reeves,Keanu,9315.45,10000.00,1/17/1998,A
        312,Butler,Gerard,90.00,1000.00,8/6/2003,B
        868,Hewitt,Jennifer Love,"0",17000.00,5/25/1985,B
        761,Pinkett-Smith,Jada,49654.87,100000.00,12/5/2006,A
        317,Murray,Bill,789.65,5000.00,2/5/2007,C
     */
}
