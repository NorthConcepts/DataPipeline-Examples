package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetSequenceNumberField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddASequenceNumberColumn {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        TransformingReader transformingReader = new TransformingReader(reader);

        transformingReader.add(new SetSequenceNumberField("SequenceNumber", 1, 2));

        Job.run(transformingReader, new StreamWriter(System.out));
    }
}
