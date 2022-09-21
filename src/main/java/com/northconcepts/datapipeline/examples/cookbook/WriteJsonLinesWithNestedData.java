package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonRecordReader;
import com.northconcepts.datapipeline.json.JsonLinesWriter;

import java.io.File;

public class WriteJsonLinesWithNestedData {
    public static void main(String[] args) {
        DataReader reader = new JsonRecordReader(new File("data/input/nested-data.json"))
                .addRecordBreak("/array/object");

        DataWriter writer = new JsonLinesWriter(new File("data/output/json-lines-with-nested-data.jsonl"));

        Job.run(reader, writer);
    }
}
