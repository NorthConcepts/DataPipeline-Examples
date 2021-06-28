/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.amazons3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteParquetToAmazonS3UsingATemporaryFile {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {

        File parquetFile = File.createTempFile("credit-balance", ".parquet");
        parquetFile.deleteOnExit();

        try {
            DataReader reader = new CSVReader(new File("example/data/input/credit-balance.csv"))
                .setFieldNamesInFirstRow(true);
            ParquetDataWriter writer = new ParquetDataWriter(parquetFile);

            Job.run(reader, writer);

            uploadFileToS3(parquetFile);
        } finally {
            parquetFile.delete();
        }
    }

    private static void uploadFileToS3(File parquetFile) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        try {
            s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            s3.open();

            OutputStream out = s3.writeMultipartFile("datapipeline-test-01", "output/credit-balance.parquet");
            InputStream in = new BufferedInputStream(new FileInputStream(parquetFile));

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
            }
        } finally {
            s3.close();
        }
    }
}
