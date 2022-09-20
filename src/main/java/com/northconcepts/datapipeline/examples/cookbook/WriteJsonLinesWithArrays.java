package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonRecordReader;
import com.northconcepts.datapipeline.json.JsonLinesWriter;

import java.io.File;

public class WriteJsonLinesWithArrays {
    public static void main(String[] args) {
        DataReader reader = new JsonRecordReader(new File("data/input/arrays.jsonl"))
                .addRecordBreak("/array");

        DataWriter writer = new JsonLinesWriter(new File("data/output/json-lines-with-arrays.jsonl"))
                .setPretty(true);

        Job.run(reader, writer);
    }
}
