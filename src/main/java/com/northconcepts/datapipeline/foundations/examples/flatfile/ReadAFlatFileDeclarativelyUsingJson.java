/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.flatfile;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;

public class ReadAFlatFileDeclarativelyUsingJson {

    public static void main(String[] args) throws Throwable {
        DataMappingPipeline pipeline = new DataMappingPipeline().fromJson(ReadAFlatFileDeclarativelyUsingJson.class.getResourceAsStream("ReadAFlatFileDeclaratively.json"));

        System.out.println("---------------------");
        System.out.println(pipeline.toXml());
        System.out.println("---------------------");
        System.out.println(Util.formatJson(pipeline.toJson()));
        System.out.println("---------------------");


        DataReader reader = pipeline.createDataReader();
        DataWriter writer = StreamWriter.newSystemOutWriterWithSessionProperties();

        Job.run(reader, writer);
    }

}
