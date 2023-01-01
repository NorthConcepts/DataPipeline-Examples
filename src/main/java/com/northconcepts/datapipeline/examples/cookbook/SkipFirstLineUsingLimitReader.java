package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.LimitReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;

public class SkipFirstLineUsingLimitReader {

    private static final int MAX_RESULTS = 3;
    private static final int SKIPPED_LINES = 1;
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new LimitReader(reader, SKIPPED_LINES, MAX_RESULTS);

        Job.run(reader, new StreamWriter(System.out));
    }
    
}
