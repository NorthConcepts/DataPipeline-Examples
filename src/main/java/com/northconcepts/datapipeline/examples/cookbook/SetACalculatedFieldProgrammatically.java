/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.job.JobTemplate;
import com.northconcepts.datapipeline.transform.Transformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class SetACalculatedFieldProgrammatically {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        TransformingReader transformingReader = new TransformingReader(reader);
        
        transformingReader.add(new Transformer() {
            public boolean transform(Record record) throws Throwable {
                // creates an 'AvailableCredit' field
                Field availableCredit = record.getField("AvailableCredit", true);
                
                Field creditLimit = record.getField("CreditLimit");
                Field balance = record.getField("Balance");
                
                // Since CSV fields are strings, they need to be parsed before subtraction
                availableCredit.setValue(
                        Double.parseDouble(creditLimit.getValueAsString()) -
                        Double.parseDouble(balance.getValueAsString())
                );
                
                return true;
            }
        });
        
        Job.run(transformingReader, new StreamWriter(System.out));
    }
    
}
