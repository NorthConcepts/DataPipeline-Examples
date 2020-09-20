/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.RemoveDuplicatesReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class RemoveDuplicates {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-05.csv"))
            .setFieldNamesInFirstRow(true);
        
        // returns records with unique 'Rating-CreditLimit' pairs
        reader = new RemoveDuplicatesReader(
                reader,
                new FieldList("Rating", "CreditLimit")
                );
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
