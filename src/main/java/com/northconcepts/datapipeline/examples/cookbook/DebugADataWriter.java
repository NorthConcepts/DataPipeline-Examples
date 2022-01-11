/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.DebugWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;

public class DebugADataWriter {

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-04.csv"))
                .setFieldNamesInFirstRow(true);

        // print each record to System.out before they are written,
        // prefixing the output with the text "to file"
        writer = new DebugWriter(writer, "to file", System.out);

        Job.run(reader, writer);
    }

}
