package com.northconcepts.datapipeline.examples.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class WriteMyOwnTransformer {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader)
            .setExceptionOnFailure(false)
            .add(new MyTransformer());
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
