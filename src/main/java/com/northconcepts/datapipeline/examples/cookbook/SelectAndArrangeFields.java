package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class SelectAndArrangeFields {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        // remove all fields except 'FirstName', 'LastName', and 'Rating'; 
        // arrange them in that order
        reader = new TransformingReader(reader)
            .add(new SelectFields("FirstName", "LastName", "Rating"));
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
