/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.meter.Meter;
import com.northconcepts.datapipeline.meter.MeteredReader;

public class ObtainStatistics {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) throws Throwable {
        MeteredReader reader = new MeteredReader(new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true));
        
        ExcelDocument document = new ExcelDocument();
        DataWriter writer = new ExcelWriter(document)
            .setSheetName("balance");

        Job job = Job.run(reader, writer);
        document.save(new File("example/data/output/credit-balance-04.xls"));
        
        final long ended = System.currentTimeMillis();
        
        Meter meter = reader.getMeter();
        
        log.info("started: " + meter.getStarted());
        log.info("ended: " + ended);
        log.info("running time: " + ((ended - meter.getStarted()) / 1000.0) + " seconds");
        log.info("meter time: " + (meter.getElapsedTime() / 1000.0) + " seconds");
        log.info("records read: " + meter.getCount());
        log.info("rate: " + meter.getUnitsPerSecondAsString());
        log.info("records transferred: " + job.getRecordsTransferred());
        log.info("started on: " + job.getStartedOn());
        log.info("finished on: " + job.getFinishedOn());
        log.info("running time: " + job.getRunningTimeAsString());
    }

}
