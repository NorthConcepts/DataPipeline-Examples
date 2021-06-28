/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.sql.Date;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.format.Rounder;

public class ManipulateFields {

    public static void main(String[] args) {
        // Setup test data
        //--------------------------------------------------------------
        RecordList recordList = new RecordList();

        Record record1 = new Record();
        record1.setField("Account", "JW19850512AST");
        record1.setField("Name", "John Wayne");
        record1.setField("Balance", 156.35);
        record1.setField("LastPaymentDate", new Date(2007-1900, 1, 13));
        recordList.add(record1);

        Record record2 = new Record();
        record2.getField("Account", true).setValue("PP20010204PIJ");
        record2.getField("Name", true).setValue("Peter Parker");
        record2.getField("Balance", true).setValue(-120.85);
        record2.getField("LastPaymentDate", true).setNull(FieldType.DATE);
        recordList.add(record2);
        
        DataReader reader = new MemoryReader(recordList);
        DataWriter writer = new StreamWriter(System.out);
        
        // Setup transformations
        //--------------------------------------------------------------
        TransformingReader transformingReader = new TransformingReader(reader);
        
        transformingReader.add(new BasicFieldTransformer("LastPaymentDate")
            .dateToString("EEE MMM d, yyyy")  // 'LastPaymentDate' to string
            .nullToValue("No payments")  // convert any null 'LastPaymentDate' to 'No payments'
            );

        transformingReader.add(new BasicFieldTransformer("Balance")
            .round(new Rounder(Rounder.RoundingPolicy.HALF_ODD, 1)) // round 'Balance' to 1 decimal
            .numberToString("$#,##0.00;($#,##0.00)")  // format 'Balance' as string
            );
        
        transformingReader.add(new BasicFieldTransformer("Account")
            .substring(2, 10)  // extract date-string from 'Account'
            .stringToDate("yyyyMMdd") // parse date-string
            .dateTimeToString("'Created' EEE MMM d, yyyy") // format date as string
            );
        
        // Run transformation job
        //--------------------------------------------------------------
        Job.run(transformingReader, writer);
    }
    
}
