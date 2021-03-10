package com.northconcepts.datapipeline.examples.amazons3;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class WriteParquetToAmazonS3UsingATemporaryFile {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3.open();

        try {
            File PARQUET_FILE = File.createTempFile("credit-balance", ".parquet");
            PARQUET_FILE.deleteOnExit();

            DataReader reader = new CSVReader(new File("example/data/input/credit-balance.csv"))
                    .setFieldNamesInFirstRow(true);
            ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);

            Job.run(reader, writer);

            OutputStream out = s3.writeMultipartFile("datapipeline-test-01", "output/credit-balance.parquet");
            InputStream in = new BufferedInputStream(
                    new FileInputStream(PARQUET_FILE));

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
