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
import com.northconcepts.datapipeline.orc.OrcDataWriter;

public class WriteAnOrcFileToAmazonS3UsingATemporaryFile {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        File orcFile = File.createTempFile("credit-balance", ".orc");
        orcFile.deleteOnExit();

        try {
            DataReader reader = new CSVReader(new File("example/data/input/credit-balance.csv"))
                .setFieldNamesInFirstRow(true);
            OrcDataWriter writer = new OrcDataWriter(orcFile);

            Job.run(reader, writer);

            uploadFileToS3(orcFile);
        } finally {
            orcFile.delete();
        }
    }

    private static void uploadFileToS3(File orcFile) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        try {
            s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            s3.open();

            OutputStream out = s3.writeMultipartFile("bucket-name", "output/credit-balance.orc");
            InputStream in = new BufferedInputStream(new FileInputStream(orcFile));

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
