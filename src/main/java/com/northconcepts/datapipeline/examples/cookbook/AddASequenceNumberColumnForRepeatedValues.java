/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.math.BigDecimal;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetGroupSequenceNumberField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddASequenceNumberColumnForRepeatedValues {

    public static void main(String[] main) {
        DataReader reader = new CSVReader(new File("example/data/input/hospital.csv"))
                .setFieldNamesInFirstRow(true);

        TransformingReader transformingReader = new TransformingReader(reader);

        transformingReader.add(new SetGroupSequenceNumberField("Ownership_SequenceNumber",
                new BigDecimal(1), new BigDecimal(1), "Hospital Ownership"));

        Job.run(transformingReader, new StreamWriter(System.out));
    }
}
