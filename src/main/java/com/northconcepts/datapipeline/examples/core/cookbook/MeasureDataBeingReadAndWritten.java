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
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.meter.MeteredReader;
import com.northconcepts.datapipeline.meter.MeteredWriter;

public class MeasureDataBeingReadAndWritten {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-04.csv"))
            .setFieldNamesInFirstRow(true);

        MeteredReader meteredReader = new MeteredReader(reader);
        MeteredWriter meteredWriter = new MeteredWriter(writer);

        Job job = Job.run(meteredReader, meteredWriter);
        
        log.info("read rate: " + meteredReader.getMeter().getUnitsPerSecondAsString());
        log.info("write rate: " + meteredWriter.getMeter().getUnitsPerSecondAsString());
        log.info("records transferred: " + job.getRecordsTransferred());
        log.info("running time: " + job.getRunningTimeAsString());
    }

}
