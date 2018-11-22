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
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.DataReaderLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;

public class LookupDataInAnyDataReader {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        /* This lookup matches 
         *    [credit-balance-01.csv].[Rating] to [rating-table-01.csv].[rating_code]
         * to return    
         *   [rating-table-01.csv].[rating_description]
         */ 
        Lookup lookup = new DataReaderLookup(
                new CSVReader(new File("example/data/input/rating-table-01.csv"))
                    .setFieldNamesInFirstRow(true),
                new FieldList("rating_code"), 
                new FieldList("rating_description")
                );
        
        reader = new TransformingReader(reader)
                .add(new LookupTransformer(new FieldList("Rating"),  lookup));
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
