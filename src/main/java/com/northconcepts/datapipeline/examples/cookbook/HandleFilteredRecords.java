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
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.fixedwidth.FixedWidthReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class HandleFilteredRecords {

    public static void main(String[] args) {
        // save discarded records in memory, but can be any DataWriter
        MemoryWriter discardWriter = new MemoryWriter();
        
        // add a "failed_filter_rule" field to each record containing the reason it was discarded 
        String discardReasonFieldName = "failed_filter_rule";
        
        DataReader reader = new FixedWidthReader(new File("example/data/input/credit-balance-01.fw"))
                .addFields(8)
                .addFields(16)
                .addFields(16)
                .addFields(12)
                .skipField(14)  // ignore CreditLimit field
                .skipField(16)  // ignore AccountCreated field
                .skipField(7)   // ignore Rating field
                .setFieldNamesInFirstRow(true);
        
        // Since Balance field is a string, need to convert it to double
        reader = new TransformingReader(reader)
                .add(new SetCalculatedField("Balance", "parseDouble(Balance)"));
        
        reader = new FilteringReader(reader, discardWriter, discardReasonFieldName)
                .add(new FilterExpression("Balance >= 1000"));

        DataWriter writer = new StreamWriter(System.out);
        
        // write the filtered records to STDOUT
        Job.run(reader, writer);
        
        // write the discarded records to STDOUT
        System.out.println("\n---- The discarded records ----");
        Job.run(new MemoryReader(discardWriter.getRecordList()), writer);
    }
}
