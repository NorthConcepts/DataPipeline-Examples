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
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class SetACalculatedFieldAtRuntime {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        // Since CSV fields are strings, they need to be parsed before subtraction
        reader = new TransformingReader(reader)
                .add(new SetCalculatedField("AvailableCredit", "parseDouble(CreditLimit) - parseDouble(Balance)"));
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
