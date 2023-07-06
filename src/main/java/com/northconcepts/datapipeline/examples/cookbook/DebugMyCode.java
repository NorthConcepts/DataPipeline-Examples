package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;

public class DebugMyCode {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);

        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-04.csv"))
            .setFieldNamesInFirstRow(true);

        // print each record to System.out as they are read, 
        // prefixing the output with the text "from file"
        DebugReader debugReader = new DebugReader(reader, "from file", System.out);
    
        Job.run(debugReader, writer);
    }

}
