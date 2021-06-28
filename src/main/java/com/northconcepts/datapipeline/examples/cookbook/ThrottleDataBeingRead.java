/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.throttle.ThrottledReader;

public class ThrottleDataBeingRead {
    
    public static final Logger log = DataEndpoint.log; 

    private static final int MAX_BYTES_PER_SECOND = 1024;

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-04.csv"))
            .setFieldNamesInFirstRow(true);

        ThrottledReader throttledReader = new ThrottledReader(reader, MAX_BYTES_PER_SECOND);

        Job.run(throttledReader, writer);
        log.info("read rate: " + throttledReader.getMeter().getUnitsPerSecondAsString());
    }

}
