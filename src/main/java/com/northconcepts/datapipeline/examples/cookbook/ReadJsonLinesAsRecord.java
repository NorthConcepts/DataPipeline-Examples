package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonRecordReader;

import java.io.File;

public class ReadJsonLinesAsRecord {
    public static void main(String[] args) {
        DataReader reader = new JsonRecordReader(new File("data/input/json-input-with-lines.jsonl"))
                .addRecordBreak("/object");

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
