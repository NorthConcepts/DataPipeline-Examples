/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
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
import com.northconcepts.datapipeline.transform.FastRenameField;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class RenameAFieldQuicklyInFlatAndTabularData {

    /**
     * An performant alternative to {@link RenameField} that assumes
     * 1) a flat table structure (no nested fields or arrays),
     * 2) columns are always in the same position from row to row
     * 3) no missing fields (nulls field values are okay), and
     * 4) the target name does not already exist (otherwise there will be two fields with the same name).
     */
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new FastRenameField("Rating", "Score")) // Rename field with name
                .add(new FastRenameField(1, "last_name")) // Rename field "LastName" to "last_name" by index
                .add(new FastRenameField(2, "first_name")) // Rename field "LastName" to "last_name" by index
                ;

        Job.run(reader, new StreamWriter(System.out));
    }

}
