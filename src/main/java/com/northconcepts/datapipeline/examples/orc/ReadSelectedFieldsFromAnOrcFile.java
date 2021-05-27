package com.northconcepts.datapipeline.examples.orc;

import java.io.File;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class ReadSelectedFieldsFromAnOrcFile {

    public static void main(String[] args) {
        
        FieldList columns = new FieldList("_col0", "_col4", "_col07", "_col9");

        OrcDataReader reader = new OrcDataReader(new File("example/data/input/userdata1.orc"))
                .setColumns(columns) // Comment this line to read all columns.
                ;
        Job.run(reader, new StreamWriter(System.out));

        System.out.println(reader.getSchema());
    }
}
