/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.fixedwidth.FixedWidthWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteACsvFileToFixedWidth {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        FixedWidthWriter writer = new  FixedWidthWriter(new File("example/data/output/credit-balance-02.fw"));
        writer.addFields(8);
        writer.addFields(16);
        writer.addFields(16);
        writer.addFields(12);
        writer.addFields(14);
        writer.addFields(16);
        writer.addFields(7);
        
        Job.run(reader, writer);
    }

}
