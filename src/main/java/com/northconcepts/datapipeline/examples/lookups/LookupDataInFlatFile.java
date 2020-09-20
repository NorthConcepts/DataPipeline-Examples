/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.lookups;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.DataReaderLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;

public class LookupDataInFlatFile {

    private static final String INPUT_FOLDER = "data/input";
    private static final String OUTPUT_FOLDER = "data/output";

    public static void main(String[] args) {
        
        DataReader reader = new CSVReader(new File(INPUT_FOLDER, "dbip-country-lite-2018-10.csv"))
                .setFieldNamesInFirstRow(false)
                .setFieldNames("ip_start", "ip_end", "country");
        
        Lookup lookupCountries = new DataReaderLookup(
                new CSVReader(new File(INPUT_FOLDER, "countries.csv")).setFieldSeparator(';').setFieldNamesInFirstRow(true),
                new FieldList("Country code"));        
        
        reader = new TransformingReader(reader)
                .add(new LookupTransformer(new FieldList("country"), lookupCountries).setAllowNoResults(false))
                .setExceptionOnFailure(false);
        
        DataWriter writer = new CSVWriter(new File(OUTPUT_FOLDER, "ip-countries.csv"))
                .setFieldNamesInFirstRow(true);
        
        Job.run(reader, writer);
    }

}
