package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;

public class ReadATsvFile {
    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/input-file.tsv"))
                .setFieldSeparator("\t")
                .setFieldNamesInFirstRow(true);

        Job.run(reader, new StreamWriter(System.out));
    }
}
