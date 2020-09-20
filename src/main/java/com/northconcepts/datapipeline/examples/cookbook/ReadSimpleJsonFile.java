/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonReader;

public class ReadSimpleJsonFile {

    public static void main(String[] args) {
        DataReader reader = new SimpleJsonReader(new File("example/data/output/simple-json-to-file.json"));
        
        Job.run(reader, new StreamWriter(System.out));
    }
}
