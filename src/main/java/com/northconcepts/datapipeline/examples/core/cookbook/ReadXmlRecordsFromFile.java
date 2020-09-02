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
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlRecordReader;

public class ReadXmlRecordsFromFile {

    public static void main(String[] args) {
        DataReader reader = new XmlRecordReader(new File("example/data/output/simple-xml-to-file.xml"))
                .addRecordBreak("/records/record");
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }
}
