package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;

import java.io.File;

public class ReadJsonLines {
    public static void main(String[] args) {
        DataReader reader = new JsonReader(new File("data/input/json-input-with-lines.jsonl"))
                .addField("empMID", "/object/Employee/array/object/empMID")
                .addField("comments", "/object/Employee/array/object/comments")
                .addField("col1", "/object/Employee/array/object/col1")
                .addField("address", "/object/Employee/array/object/contact/array/object/address")
                .addRecordBreak("/object/Employee/array/object");

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
