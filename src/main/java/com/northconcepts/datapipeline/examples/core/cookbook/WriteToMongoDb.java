/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.mongodb.MongoWriter;

public class WriteToMongoDb {

    public static void main(String[] args) {
        String database = "datapipeline";
        String collection = "clients";
        
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);

        DataWriter writer = new MongoWriter(database, collection);
        
        Job.run(reader, writer);
    }
    /* CSV input
        Account,LastName,FirstName,Balance,CreditLimit,AccountCreated,Rating
        101,Reeves,Keanu,9315.45,10000.00,1/17/1998,A
        312,Butler,Gerard,90.00,1000.00,8/6/2003,B
        868,Hewitt,Jennifer Love,0,17000.00,5/25/1985,B
        761,Pinkett-Smith,Jada,49654.87,100000.00,12/5/2006,A
        317,Murray,Bill,789.65,5000.00,2/5/2007,C
     */
    
    /* to check MongoDb server, run the mongo shell
        > use datapipeline
        switched to db datapipeline
        > db.clients.find()
        { "_id" : ObjectId("57026fef8dbd7a08b4846d84"), "Account" : "101", "LastName" :
        "Reeves", "FirstName" : "Keanu", "Balance" : "9315.45", "CreditLimit" : "10000.0
        0", "AccountCreated" : "1/17/1998", "Rating" : "A" }
        { "_id" : ObjectId("57026fef8dbd7a08b4846d85"), "Account" : "312", "LastName" :
        "Butler", "FirstName" : "Gerard", "Balance" : "90.00", "CreditLimit" : "1000.00"
        , "AccountCreated" : "8/6/2003", "Rating" : "B" }
        { "_id" : ObjectId("57026fef8dbd7a08b4846d86"), "Account" : "868", "LastName" :
        "Hewitt", "FirstName" : "Jennifer Love", "Balance" : "0", "CreditLimit" : "17000
        .00", "AccountCreated" : "5/25/1985", "Rating" : "B" }
        { "_id" : ObjectId("57026fef8dbd7a08b4846d87"), "Account" : "761", "LastName" :
        "Pinkett-Smith", "FirstName" : "Jada", "Balance" : "49654.87", "CreditLimit" : "
        100000.00", "AccountCreated" : "12/5/2006", "Rating" : "A" }
        { "_id" : ObjectId("57026fef8dbd7a08b4846d88"), "Account" : "317", "LastName" :
        "Murray", "FirstName" : "Bill", "Balance" : "789.65", "CreditLimit" : "5000.00",
         "AccountCreated" : "2/5/2007", "Rating" : "C" }
        >
     */
}
