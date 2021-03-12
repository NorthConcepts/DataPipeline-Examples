package com.northconcepts.datapipeline.examples.amazons3;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadParquetFromAmazonS3UsingATemporaryFile {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3.open();

        try {
            File parquetFile = File.createTempFile("output", ".parquet");
            parquetFile.deleteOnExit();

            try {
                InputStream in = s3.readFile("bucket", "input.parquet");
                OutputStream out = new BufferedOutputStream(new FileOutputStream(parquetFile));

                byte[] buffer = new byte[1024];
                int lengthRead;
                while ((lengthRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, lengthRead);
                    out.flush();
                }

                DataReader reader = new ParquetDataReader(parquetFile);
                DataWriter writer = new StreamWriter(System.out);

                Job.run(reader, writer);
            } finally {
                parquetFile.delete();
            }
        } finally {
            s3.close();
        }
    }
}
