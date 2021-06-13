/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;


import java.io.FileInputStream;

import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;

public class BuildDataMappingPipelineDeclarativelyFromJson {

    public static void main(String[] args) throws Throwable {
        DataMappingPipeline pipeline = new DataMappingPipeline().fromJson(new FileInputStream("example/data/input/pipeline/datamappingpipeline.json"));

        //Run pipeline
        pipeline.run();
    }

}
