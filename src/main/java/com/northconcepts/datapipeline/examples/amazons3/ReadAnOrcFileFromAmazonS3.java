/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.amazons3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class ReadAnOrcFileFromAmazonS3 {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        Path path = new Path("s3a://bucketName/input.orc");

        Configuration configuration = new Configuration();
        configuration.set("fs.s3a.access.key", ACCESS_KEY);
        configuration.set("fs.s3a.secret.key", SECRET_KEY);
        configuration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");

        DataReader reader = new OrcDataReader(path)
                    .setConfig(configuration);
        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
