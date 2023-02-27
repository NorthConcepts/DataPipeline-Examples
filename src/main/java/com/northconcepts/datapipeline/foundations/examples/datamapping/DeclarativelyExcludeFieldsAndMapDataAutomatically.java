/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;
import java.io.FileInputStream;

public class DeclarativelyExcludeFieldsAndMapDataAutomatically {

    public static void main(String[] args) throws Throwable {
    
        //Load DataMapping from this XML File.
        DataMapping mappingFromXml = new DataMapping()
                .fromXml(new FileInputStream("example/data/input/datamapping/customer-details-data-mapping.xml"));

        DataReader reader = new CSVReader(new File("example/data/input/bank_account.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);

        reader = new DataMappingReader(reader, mappingFromXml);

        Job.run(reader, new StreamWriter(System.out));
    }

}
