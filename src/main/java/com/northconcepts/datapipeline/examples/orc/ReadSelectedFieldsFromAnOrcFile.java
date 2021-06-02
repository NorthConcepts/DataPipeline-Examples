package com.northconcepts.datapipeline.examples.orc;

import java.io.File;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class ReadSelectedFieldsFromAnOrcFile {

    public static void main(String[] args) {
        FieldList columns = new FieldList("list", "double1", "short1", "string1");

        OrcDataReader reader = new OrcDataReader(new File("example/data/input/input_orc_file.orc"))
                .setColumns(columns) // Remove this line to read all columns.
                ;
        Job.run(reader, new StreamWriter(System.out));

        System.out.println(reader.getSchema());
    }
}
