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
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonReader;
import com.northconcepts.datapipeline.rtf.RtfWriter;

public class GenerateAWordDoc {

    public static void main(String[] args) {
        DataReader reader = new SimpleJsonReader(
                new File("example/data/input/simple-json-input.json"));
        
        DataWriter writer = new RtfWriter(
                new File("example/data/output/simple-json-output.doc"));
        
        Job.run(reader, writer);
    }

}
