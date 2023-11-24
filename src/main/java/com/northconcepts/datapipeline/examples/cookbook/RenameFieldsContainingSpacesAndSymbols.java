package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldPath;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.FastRenameField;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;

public class RenameFieldsContainingSpacesAndSymbols {
    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/countries-1.csv"))
                .setFieldNamesInFirstRow(true);
        reader = new TransformingReader(reader)
                .add(new RenameField(FieldPath.fromName("Country (en)"), "Country"))
                .add(new RenameField(FieldPath.fromName("Currency code"), "Currency Abbr."))
                .add(new FastRenameField("Country (local)", "Country Local Name"))
                .add(new FastRenameField("Government form", "Government Type"));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
