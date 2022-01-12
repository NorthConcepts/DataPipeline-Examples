/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.flatfile2;

import java.io.FileReader;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;

public class ReadAFlatFile {

    public static void main(String[] args) throws Throwable {
        DataReader reader = new FlatFileReader(new FileReader("example/data/input/flatfile-01.txt"))
                .addFixedLengthField("id", 10)
                .addFixedLengthField("year", 4)
                .addFixedLengthField("month", 2)
                .addFixedLengthField("day", 2)
                .addVariableLengthField("firstName", "@@")
                .addVariableLengthField("lastName", "!")
                .setSaveLineage(true);

        DataWriter writer = StreamWriter.newSystemOutWriterWithSessionProperties();

        Job.run(reader, writer);
    }

}
