/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
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
