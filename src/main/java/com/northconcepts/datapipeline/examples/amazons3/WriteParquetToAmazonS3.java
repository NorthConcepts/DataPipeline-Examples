/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.amazons3;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.util.HadoopOutputFile;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteParquetToAmazonS3 {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        Path path = new Path("s3a://bucketName/output.parquet");

        Configuration configuration = new Configuration();
        configuration.set("fs.s3a.access.key", ACCESS_KEY);
        configuration.set("fs.s3a.secret.key", SECRET_KEY);
        configuration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");

        HadoopOutputFile outputFile = HadoopOutputFile.fromPath(path, configuration);

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance.csv"))
                    .setFieldNamesInFirstRow(true);

        ParquetDataWriter writer = new ParquetDataWriter(outputFile);

        Job.run(reader, writer);
    }
}
