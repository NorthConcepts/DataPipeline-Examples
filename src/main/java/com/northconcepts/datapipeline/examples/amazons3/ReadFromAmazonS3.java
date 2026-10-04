package com.northconcepts.datapipeline.examples.amazons3;

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
    private static final String BUCKET = "YOUR BUCKET";
    private static final String KEY = "output/trades.csv";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3.open();
        try {
            InputStream inputStream = s3.readFile(BUCKET, KEY);

            DataReader reader = new CSVReader(new InputStreamReader(inputStream));
            DataWriter writer = new NullWriter();
            Job.run(reader, writer);
            
            System.out.println("Records read: " + writer.getRecordCount());
        } finally {
            s3.close();
        }
    }

}
