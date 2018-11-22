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
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.fixedwidth.FixedWidthWriter;
import com.northconcepts.datapipeline.job.Job;

/**
 * Compiling and running a job is the same as for any other java class:
 * 
 * Compiling:   javac -classpath NorthConcepts-DataPipeline.jar CompileAndRunAJob.java
 *
 * Running:     java -classpath NorthConcepts-DataPipeline.jar;antlr-2.7.5.jar;poi-3.0-alpha2-20060616.jar;poi-contrib-3.0-alpha2-20060616.jar;poi-scratchpad-3.0-alpha2-20060616.jar;jxl-2.6.3.jar;itext-2.0.5.jar CompileAndRunAJob
 * 
 */
public class CompileAndRunAJob {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);

        FixedWidthWriter writer = new FixedWidthWriter(new File("example/data/output/credit-balance-02.fw"));
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
