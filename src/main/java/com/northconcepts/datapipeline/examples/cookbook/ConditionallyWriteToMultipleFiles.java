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
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.multiplex.SplitWriter;

public class ConditionallyWriteToMultipleFiles {

    private static final String INPUT = "example/data/input";
    private static final String OUTPUT = "example/data/output";

    public static void main(String[] args) {
        
        DataReader reader = new CSVReader(new File(INPUT, "countries.csv"))
                .setFieldSeparator(';')
                .setFieldNamesInFirstRow(true);

        SplitWriter splitWriter = new SplitWriter();
        
        // Async job writing to USD file
        DataReader reader1 = splitWriter.createReader();
        reader1 = new FilteringReader(reader1).add(new FilterExpression("${Currency code} == 'USD'"));
        DataWriter writer1 = new CSVWriter(new File(OUTPUT, "countries-usd.csv"))
                .setFieldSeparator(',')
                .setFieldNamesInFirstRow(true);
        Job.runAsync(reader1, writer1);
        

        // Async job writing to non-USD file
        DataReader reader2 = splitWriter.createReader();
        reader2 = new FilteringReader(reader2).add(new FilterExpression("${Currency code} != 'USD'"));
        DataWriter writer2 = new CSVWriter(new File(OUTPUT, "countries-not-usd.csv"))
                .setFieldSeparator(',')
                .setFieldNamesInFirstRow(true);
        Job.runAsync(reader2, writer2);
        
        
        // Sync job writing to async jobs
        Job.run(reader, splitWriter);
    }

}
