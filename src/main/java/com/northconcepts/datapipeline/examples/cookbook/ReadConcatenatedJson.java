package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonLinesWriter;
import com.northconcepts.datapipeline.json.JsonRecordReader;

import java.io.File;

public class ReadConcatenatedJson {
    public static void main(String[] args) {
        JsonRecordReader reader = new JsonRecordReader(new File("data/input/concatenated-json.jsonl"))
                .addRecordBreak("/object");

        DataWriter writer = new JsonLinesWriter(new File("data/output/output-json.jsonl"));

        Job.run(reader, writer);
    }
}
