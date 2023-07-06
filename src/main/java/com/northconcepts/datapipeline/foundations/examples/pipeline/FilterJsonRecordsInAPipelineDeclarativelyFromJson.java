package com.northconcepts.datapipeline.foundations.examples.pipeline;


import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;

import java.io.FileInputStream;

public class FilterJsonRecordsInAPipelineDeclarativelyFromJson {

    public static void main(String[] args) throws Throwable {
        Pipeline pipeline = new Pipeline()
            .fromJson(new FileInputStream("example/data/input/pipeline/json-records-pipeline.json"));
        pipeline.run();
    }
}
