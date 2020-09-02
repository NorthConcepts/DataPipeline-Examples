/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.xml.XmlReader;

public class ReadAnXmlFile {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) {
        DataReader reader = new XmlReader(new File("example/data/input/bookstore.xml"))
        	.addField("title", "//book/title/text()")
        	.addField("language", "//book/title/@lang")
        	.addField("price", "//book/price/text()")
        	.addRecordBreak("//book");

        reader.open();
        try {
            Record record;
            while ((record = reader.read()) != null) {
                log.info(record);
            }
        } finally {
            reader.close();
        }
    }

}
