/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;


import java.nio.file.Files;
import java.nio.file.Paths;

import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;

public class BuildDataMappingPipelineDeclarativelyFromJson {

    public static void main(String[] args) throws Throwable {
        String jsonString = new String(Files.readAllBytes(Paths.get("example/data/input/pipeline/datamappingpipeline.json")));
        DataMappingPipeline pipeline = new DataMappingPipeline().fromJson(jsonString);
        //pipeline.setOutput(new DataWriterPipelineOutput(() -> StreamWriter.newSystemOutWriter()));
        //pipeline.run();

        System.out.println(pipeline.toXml());
    }

}
