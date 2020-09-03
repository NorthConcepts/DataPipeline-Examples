/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddMyOwnFunctions {
    
    // functions are public, static methods
    public static String initials(String firstName, String lastName) {
        return firstName.substring(0, 1).toUpperCase() +
            " " +
            lastName.substring(0, 1).toUpperCase();
    }
    
    public static void main(String[] args) throws Throwable {
        // register new function 'initials'
        Functions.add("initials", "com.northconcepts.datapipeline.examples.core.cookbook.customization.AddMyOwnFunctions.initials");
        
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        TransformingReader transformingReader = new TransformingReader(reader);
        
        // use the new function in a SetCalculatedField or FilterExpression 
        transformingReader.add(new SetCalculatedField("name", 
                "initials(FirstName, LastName)"));
        
        Job.run(transformingReader, new StreamWriter(System.out));
    }
    
}
