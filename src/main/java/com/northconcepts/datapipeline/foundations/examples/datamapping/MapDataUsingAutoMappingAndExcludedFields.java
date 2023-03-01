/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.job.Job;

public class MapDataUsingAutoMappingAndExcludedFields {

    public static void main(String[] args) {
        DataMapping mapping = new DataMapping()
                .setAutoMapping(true) // enable auto fields mapping
                .addFieldMapping("Full_Name", "source.LastName+ ' ' + source.FirstName")
                .addExcludedFields("AccountCreated", "Rating");
        
        DataReader reader = new CSVReader(new File("example/data/input/bank_account.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);

        reader = new DataMappingReader(reader, mapping);

        Job.run(reader, new StreamWriter(System.out));
    }

}
