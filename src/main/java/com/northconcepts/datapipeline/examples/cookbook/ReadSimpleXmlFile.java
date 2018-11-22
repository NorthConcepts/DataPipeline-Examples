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
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.SimpleXmlReader;

public class ReadSimpleXmlFile {

    public static void main(String[] args) {
        DataReader reader = new SimpleXmlReader(new File("example/data/output/simple-xml-to-file.xml"));
        
        Job.run(reader, new StreamWriter(System.out));
    }
}
