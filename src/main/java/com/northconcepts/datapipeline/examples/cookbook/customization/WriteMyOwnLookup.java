/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;

public class WriteMyOwnLookup {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        MyLookup lookup = new MyLookup();
        
        reader = new TransformingReader(reader)
            .add(new LookupTransformer(new FieldList().add("Rating"), lookup));
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
