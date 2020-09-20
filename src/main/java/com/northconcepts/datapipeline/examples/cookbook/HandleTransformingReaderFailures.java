/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
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
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.xml.XmlReader;

public class HandleTransformingReaderFailures {

    public static void main(String[] args) {
        // save discarded records in memory, but can be any DataWriter
        MemoryWriter discardWriter = new MemoryWriter();

        // add a "rename_field_exception" field to each record containing the reason it was discarded 
        String discardReasonFieldName = "rename_field_exception";
        
        DataReader reader = new XmlReader(new File("example/data/input/call-center-agents-2.xml"))
                .addField("id", "//record/@id")
                .addField("first_name", "//record/@first_name")
                .addField("last_name", "//record/@last_name")
                .addField("hired_on", "//record/@hired_on")
                .addRecordBreak("//record");
        
        // rename the field "first_name" to "last_name" for first names starting with 'B'
        // generates an exception because the "last_name" field already exists
        // discardWriter will contain all records with first names starting with 'B'
        reader =  new TransformingReader(reader, discardWriter, discardReasonFieldName)
                .setCondition(new FieldFilter("first_name", new PatternMatch("B.*")))
                .add(new RenameField("first_name", "last_name"));
        
        DataWriter writer = new StreamWriter(System.out);
        
        // write the successful condition records to STDOUT
        Job.run(reader, writer);
        
        // write the discarded records to STDOUT
        System.out.println("\n---- The discarded records ----");
        Job.run(new MemoryReader(discardWriter.getRecordList()), new StreamWriter(System.out));

    }

}
