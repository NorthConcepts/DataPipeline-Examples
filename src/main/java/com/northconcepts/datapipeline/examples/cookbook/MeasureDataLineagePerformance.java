/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class MeasureDataLineagePerformance {

    private static final boolean saveLineage = true;
    
    public static void main(String[] args) {
        readCsv("1000_Sales_Records.csv");
        readCsv("100000_Sales_Records.csv");
    }

    private static void readCsv(String fileName) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Lineage enabled: " + saveLineage);
        DataReader reader = new CSVReader(new File("example/data/input/" + fileName))
                .setFieldNamesInFirstRow(true)
                .setSaveLineage(saveLineage);

        Job job = Job.run(reader, new NullWriter());

        System.out.println("Job Running Time:- " + job.getRunningTimeAsString());
        System.out.println("Total Records transferred:- " + job.getRecordsTransferred());
    }
    
}
