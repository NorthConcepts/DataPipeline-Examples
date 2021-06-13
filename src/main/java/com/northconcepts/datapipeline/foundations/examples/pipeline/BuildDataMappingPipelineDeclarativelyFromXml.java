/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;


import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;

import java.nio.file.Files;
import java.nio.file.Paths;

public class BuildDataMappingPipelineDeclarativelyFromXml {

    public static void main(String[] args) throws Throwable {
        String xmlString = new String(Files.readAllBytes(Paths.get("example/data/input/pipeline/datamappingpipeline.xml")));
        DataMappingPipeline pipeline = (DataMappingPipeline) new DataMappingPipeline().fromXml(xmlString);

        //Run pipeline
        pipeline.run();

    }

}
