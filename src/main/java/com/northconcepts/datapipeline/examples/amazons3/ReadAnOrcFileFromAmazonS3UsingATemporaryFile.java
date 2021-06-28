/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.amazons3;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class ReadAnOrcFileFromAmazonS3UsingATemporaryFile {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    private static File orcFile;

    public static void main(String[] args) throws Throwable {
        downloadS3File();
        try {
            DataReader reader = new OrcDataReader(orcFile);
            DataWriter writer = new StreamWriter(System.out);

            Job.run(reader, writer);
        } finally {
            orcFile.delete();
        }
    }

    private static void downloadS3File() throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        try {
            s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            s3.open();

            orcFile = File.createTempFile("output", ".orc");
            orcFile.deleteOnExit();

            InputStream in = s3.readFile("bucket", "input.orc");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(orcFile));

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } finally {
            s3.close();
        }
    }
}
