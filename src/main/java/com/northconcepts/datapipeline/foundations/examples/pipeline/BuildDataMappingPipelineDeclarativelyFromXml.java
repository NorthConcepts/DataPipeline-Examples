/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;


import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;

import java.io.FileInputStream;

public class BuildDataMappingPipelineDeclarativelyFromXml {

    public static void main(String[] args) throws Throwable {
        DataMappingPipeline pipeline = new DataMappingPipeline().fromXml(new FileInputStream("example/data/input/pipeline/datamappingpipeline.xml"));
        pipeline.run();
    }

}
