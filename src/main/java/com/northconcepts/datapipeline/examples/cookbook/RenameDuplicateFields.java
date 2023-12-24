package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;

public class RenameDuplicateFields {
    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new RenameField("LastName", "Name"))
                .add(new RenameField("FirstName", "Name").setAllowDuplicateFieldNames(true))
                .add(new RenameField("Balance", "CreditLimit").setAllowDuplicateFieldNames(true));

        Job.run(reader, new StreamWriter(System.out));
    }
}
