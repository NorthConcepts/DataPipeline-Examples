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
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;

public class SpecifyLookupValuesProgrammatically {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        // create a lookup that returns a field named 'rating_description'
        BasicLookup lookup = new BasicLookup(new FieldList("rating_description"))
                .add("A", "Class-A")
                .add("B", "Class-B")
                .add("C", "Class-C");
        
        // use 'Rating' from example/data/input/credit-balance-01.csv
        reader = new TransformingReader(reader)
                .add(new LookupTransformer(new FieldList("Rating"),  lookup));  
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
