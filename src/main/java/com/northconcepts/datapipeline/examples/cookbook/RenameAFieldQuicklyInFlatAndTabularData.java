package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.FastRenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class RenameAFieldQuicklyInFlatAndTabularData {

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new FastRenameField("Rating", "Score")) // Rename field with name
                .add(new FastRenameField(1, "last_name")) // Rename field "LastName" to "last_name" by index
                .add(new FastRenameField(2, "first_name")) // Rename field "LastName" to "last_name" by index
                ;

        Job.run(reader, new StreamWriter(System.out));
    }

}
