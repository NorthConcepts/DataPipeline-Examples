/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
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

    public static void main(String[] args) {
        warmupJvm();

        readCsv("1000_Sales_Records.csv", false);
        readCsv("1000_Sales_Records.csv", true);
        readCsv("100000_Sales_Records.csv", false);
        readCsv("100000_Sales_Records.csv", true);
    }

    private static void warmupJvm() {
        System.out.println("Warming up JVM");
        
        for (int i = 0; i < 3; i++) {
            DataReader reader = new CSVReader(new File("example/data/input/100000_Sales_Records.csv"))
                    .setFieldNamesInFirstRow(true)
                    .setSaveLineage(true);
            Job.run(reader, new NullWriter());
        }
    }

    private static void readCsv(String fileName, boolean saveLineage) {
        System.out.println("--------------------------------------------------------");
        DataReader reader = new CSVReader(new File("example/data/input/" + fileName))
                .setFieldNamesInFirstRow(true)
                .setSaveLineage(saveLineage);

        Job job = Job.run(reader, new NullWriter());

        System.out.println("Lineage enabled: " + saveLineage);
        System.out.println("Records: " + job.getRecordsTransferred());
        System.out.println("Time: " + job.getRunningTimeAsString());
    }
    
}
