package com.northconcepts.datapipeline.foundations.examples.pipeline;


import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;

import java.io.FileInputStream;

public class FilterJsonRecordsInAPipelineDeclarativelyFromXml {

    public static void main(String[] args) throws Throwable {
        Pipeline pipeline = new Pipeline()
            .fromXml(new FileInputStream("example/data/input/pipeline/json-records-pipeline.xml"));
        pipeline.run();
    }
}
