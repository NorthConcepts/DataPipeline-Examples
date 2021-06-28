/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetBatchSequenceNumberField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddASequenceNumberColumnWhenValuesChange {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        TransformingReader transformingReader = new TransformingReader(reader);

        transformingReader.add(new SetBatchSequenceNumberField("BatchNumber", 10, 5, "Rating"));

        Job.run(transformingReader, new StreamWriter(System.out));
    }
}
