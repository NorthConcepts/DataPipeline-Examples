/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.text.DecimalFormat;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.fixedwidth.FixedWidthReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingWriter;

public class HandleTransformingWriterFailures {

    public static void main(String[] args) {
        // save discarded/undiscarded records in memory, but can be any DataWriter
        MemoryWriter discardWriter = new MemoryWriter();
        MemoryWriter undiscardWriter = new MemoryWriter();
        
        // add a "failed_stringToDouble_rule" field to each record containing the reason it was discarded 
        String discardReasonFieldName = "failed_stringToDouble_rule";
        
        // transform the "Rating" value to a Double, should generate an exception
        BasicFieldTransformer transformer = new BasicFieldTransformer("Rating");
        transformer.stringToDouble(new DecimalFormat());
        
        DataReader reader = new FixedWidthReader(new File("example/data/input/credit-balance-01.fw"))
                .addFields(8)
                .addFields(16)
                .addFields(16)
                .addFields(12)
                .addFields(14)
                .skipField(16)  // ignore AccountCreated field
                .addFields(7)
                .setFieldNamesInFirstRow(true);
        
        // records with "Balance > 100" causes an exception and is sent to discardWriter
        DataWriter writer = new TransformingWriter(undiscardWriter, discardWriter, discardReasonFieldName)
                .setCondition(new FilterExpression("Balance > 100"))
                .add(transformer);
        
        // Since Balance field is a string, need to convert it to double
        writer = new TransformingWriter(writer)
                .add(new SetCalculatedField("Balance", "parseDouble(Balance)"));
        
        Job.run(reader, writer);
        
        // write the undiscarded records to STDOUT
        System.out.println("\n---- The undiscarded records ----");
        Job.run(new MemoryReader(undiscardWriter.getRecordList()), new StreamWriter(System.out));

        // write the discarded records to STDOUT
        System.out.println("\n---- The discarded records ----");
        Job.run(new MemoryReader(discardWriter.getRecordList()), new StreamWriter(System.out));
        
    }

}
