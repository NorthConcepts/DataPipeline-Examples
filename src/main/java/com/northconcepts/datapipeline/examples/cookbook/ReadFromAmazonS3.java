/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromAmazonS3 {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3.open();
        try {
            InputStream inputStream = s3.readFile("datapipeline-test-01", "output/trades.csv");

            DataReader reader = new CSVReader(new InputStreamReader(inputStream));
//            DataWriter writer = StreamWriter.newSystemOutWriter();
            DataWriter writer = new NullWriter();
            Job.run(reader, writer);
            
            System.out.println("Records read: " + writer.getRecordCount());
        } finally {
            s3.close();
        }
    }

}
