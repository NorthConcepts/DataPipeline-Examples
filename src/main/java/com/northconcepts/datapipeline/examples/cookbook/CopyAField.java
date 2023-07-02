package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.CopyField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class CopyAField {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader)
                .add(new CopyField("Rating", "Score", false));   // copy 'Rating' to 'Score'; do not overwrite
        
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
