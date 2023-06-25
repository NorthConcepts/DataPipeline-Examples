package com.northconcepts.datapipeline.examples.amazons3;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class ConfigureAmazonS3FileSystemUsingClient {
    
    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";
    
    public static void main(String[] args) throws Throwable {
        BasicAWSCredentials basicCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        AmazonS3 s3Client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicCredentials))
            .withRegion(Regions.US_EAST_2)
            .build();
        
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setClient(s3Client);
        s3.open();

        try {
            InputStream inputStream = s3.readFile("datapipeline-test-01", "output/orders-records.csv");

            DataReader reader = new CSVReader(new InputStreamReader(inputStream));
            DataWriter writer = StreamWriter.newSystemOutWriter();

            Job.run(reader, writer);

            System.out.println("Records read: " + writer.getRecordCount());
        } finally {
            s3.close();
        }
    }

}
