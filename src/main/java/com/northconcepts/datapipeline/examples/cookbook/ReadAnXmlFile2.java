/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.sql.Connection;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.job.JobTemplate;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.xml.XmlReader;

public class ReadAnXmlFile2 {
    
    public static final Logger log = DataEndpoint.log;
    private static Connection connection = null; 

    public static void main(String[] args) {
        String inputFile = "example/data/input/bookstore.xml";
        String targetTable = "book";
        
        DataReader reader = new XmlReader(new File(inputFile))
        	.addField("title", "//book/title/text()")
        	.addField("language", "//book/title/@lang")
        	.addField("price", "//book/price/text()")
        	.addRecordBreak("//book");
        
        reader = new TransformingReader(reader)
            .add(new BasicFieldTransformer("price")
                .nullToValue("0")
                .replaceString("$", "")
                .trim()
                .stringToDouble());

        DataWriter writer = new  JdbcWriter(connection , targetTable)
            .setAutoCloseConnection(true);
   
        Job.run(reader, writer);
    }

}
