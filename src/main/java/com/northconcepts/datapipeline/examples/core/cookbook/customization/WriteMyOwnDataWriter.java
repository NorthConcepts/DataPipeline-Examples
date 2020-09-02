/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.examples.cookbook.customization.MyDataWriter;
import com.northconcepts.datapipeline.examples.cookbook.customization.MyProxyWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteMyOwnDataWriter {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
    
        DataWriter writer = new MyDataWriter();
        writer = new MyProxyWriter(writer);

        Job.run(reader, writer);
    }
    
}
