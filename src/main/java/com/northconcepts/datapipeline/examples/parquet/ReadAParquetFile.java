/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;

public class ReadAParquetFile {

    public static void main(String[] args) {
        ParquetDataReader reader = new ParquetDataReader(new File("example/data/input/read_parquet_file.parquet"));
        Job.run(reader, new StreamWriter(System.out));

        System.out.println("============================================================");
        System.out.println("Parquet Schema");
        System.out.println("============================================================");
        
        System.out.println(reader.getSchema());
    }
}
