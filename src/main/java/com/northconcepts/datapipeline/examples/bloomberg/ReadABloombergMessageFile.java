/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.bloomberg;

import java.io.File;

import com.northconcepts.datapipeline.bloomberg.BloombergMessage;
import com.northconcepts.datapipeline.bloomberg.BloombergMessageReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;

public class ReadABloombergMessageFile {

    public static void main(String[] args) {
        File file = new File("data/input/example-talend-stackoverflow.req");
        
        BloombergMessageReader reader = new BloombergMessageReader(file);
        DataWriter writer = new StreamWriter(System.out);
        Job.run(reader,  writer);
        
        // message is available after BloombergMessageReader.open() is called in Job.run(reader,  writer) above
        BloombergMessage message = reader.getMessage();  
        System.out.println("-----------------------------------------------");
        System.out.println("Header: " + message.getHeader().getProperties());
        System.out.println("Fields: " + message.getFields().getFields());
        System.out.println("Records: " + message.getData().getRecords().size());
        System.out.println("Footers: " + message.getFooter().getProperties());
    }

}
