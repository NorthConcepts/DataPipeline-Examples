package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.transform.Transformer;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.core.Field;

import java.io.File;

public class UseFunctionsInTransformers {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(Transformer.of(record -> {
                    Field creditLimit = record.getField("CreditLimit");
                    // increase everyone's limit by 10%
                    double newValue = Double.parseDouble(creditLimit.getValueAsString()) * 1.10;
                    creditLimit.setValue(newValue);
                    return true;
                }));

        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
