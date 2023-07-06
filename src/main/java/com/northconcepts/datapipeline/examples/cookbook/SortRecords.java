package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.SortingReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class SortRecords {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        SortingReader sortingReader = new SortingReader(reader);
        sortingReader.getOrder()
            .add("LastName", false) // descending
            .add("FirstName"); // ascending
        
        Job.run(sortingReader, new StreamWriter(System.out));
    }
    
}
