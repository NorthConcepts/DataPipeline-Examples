package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.RemoveDuplicateFields;
import com.northconcepts.datapipeline.transform.RemoveDuplicateFields.DuplicateFieldsPolicy;
import com.northconcepts.datapipeline.transform.RemoveDuplicateFields.RenamePolicy;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class RenameDuplicateFieldsWithASeparator {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new RemoveDuplicateFields(DuplicateFieldsPolicy.RENAME_UNDERSCORE));

        Job.run(reader, StreamWriter.newSystemOutWriter());

        reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new RemoveDuplicateFields(new RenamePolicy("-")));

        Job.run(reader, StreamWriter.newSystemOutWriter());
    }

}
