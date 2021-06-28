/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.validate.ValidatingReader;

public class WriteMyOwnFilterOrValidator {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        // convert balance from string to double
        reader = new TransformingReader(reader)
            .add(new BasicFieldTransformer("Balance").stringToDouble());
        
        // ensure all balances are 0.0 or more
        reader = new ValidatingReader(reader)
            .setExceptionOnFailure(true)
            .add(new MyFilter(0.0));

        // retain balances over $1000.00
        reader = new FilteringReader(reader)
            .add(new MyFilter(1000.0));
    
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
