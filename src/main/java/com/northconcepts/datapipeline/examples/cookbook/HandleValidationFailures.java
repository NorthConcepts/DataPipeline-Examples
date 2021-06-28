/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.filter.FieldFilter;
import com.northconcepts.datapipeline.filter.rule.PatternMatch;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.validate.ValidatingReader;
import com.northconcepts.datapipeline.xml.XmlReader;

public class HandleValidationFailures {

    public static void main(String[] args) {
        // save discarded records in memory, but can be any DataWriter
        MemoryWriter discardWriter = new MemoryWriter();

        // add a "failed_validation_rule" field to each record containing the reason it was discarded 
        String discardReasonFieldName = "failed_validation_rule";
        
        DataReader reader = new XmlReader(new File("example/data/input/call-center-agents-2.xml"))
                .addField("id", "//record/@id")
                .addField("first_name", "//record/@first_name")
                .addField("last_name", "//record/@last_name")
                .addField("hired_on", "//record/@hired_on")
                .addRecordBreak("//record");
        
        reader = new ValidatingReader(reader, discardWriter, discardReasonFieldName)
                .add(new FieldFilter("first_name", new PatternMatch("B.*")));
        
        DataWriter writer = new StreamWriter(System.out);
        
        // write the validated records to STDOUT
        Job.run(reader, writer);
        
        // write the discarded records to STDOUT
        System.out.println("\n---- The discarded records ----");
        Job.run(new MemoryReader(discardWriter.getRecordList()), new StreamWriter(System.out));
    }
}
