/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.AsyncReader;
import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;

public class UseMultiThreadingInASingleJob {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);

        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-04.csv"))
            .setFieldNamesInFirstRow(true);

        // buffers up to 10 MB using another thread;
        // asyncReader.read() will pull from this buffer
        AsyncReader asyncReader = new AsyncReader(reader)
            .setMaxBufferSizeInBytes(1024 * 1024 * 10);
    
        Job.run(asyncReader, writer);
        
        log.info("PeakBufferSize: " + asyncReader.getPeakBufferSizeInBytes());
    }

}
