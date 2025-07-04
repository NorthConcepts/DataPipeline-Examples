package com.northconcepts.datapipeline.foundations.examples.pipeline;


import java.io.FileInputStream;

import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;

public class BuildDataMappingPipelineDeclarativelyFromJson1 {

    public static void main(String[] args) throws Throwable {
        DataMappingPipeline pipeline = new DataMappingPipeline().fromJson(new FileInputStream("example/data/input/pipeline/datamappingpipeline-1.json"));
        pipeline.run();
    }

}
