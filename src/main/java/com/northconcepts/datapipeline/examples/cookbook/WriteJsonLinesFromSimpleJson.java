package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonReader;
import com.northconcepts.datapipeline.json.JsonLinesWriter;


import java.io.File;

public class WriteJsonLinesFromSimpleJson {
    public static void main(String[] args) {
        DataReader reader = new SimpleJsonReader(new File("data/input/simple-json-input.json"));
        DataWriter writer = new JsonLinesWriter(new File("data/output/json-lines-from-simple-json.jsonl"))
                .setPretty(true);
        

        Job.run(reader, writer);
    }
}
