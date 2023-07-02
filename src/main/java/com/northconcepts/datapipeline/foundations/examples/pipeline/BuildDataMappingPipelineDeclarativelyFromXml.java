package com.northconcepts.datapipeline.foundations.examples.pipeline;


import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;

import java.io.FileInputStream;

public class BuildDataMappingPipelineDeclarativelyFromXml {

    public static void main(String[] args) throws Throwable {
        DataMappingPipeline pipeline = new DataMappingPipeline().fromXml(new FileInputStream("example/data/input/pipeline/datamappingpipeline.xml"));
        pipeline.run();
    }

}
