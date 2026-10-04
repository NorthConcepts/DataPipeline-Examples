package com.northconcepts.datapipeline.examples.amazons3;

import java.io.InputStreamReader;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromAmazonS3UsingAnAwsProfile {

    private static final String PROFILE = "YOUR AWS PROFILE";
    private static final String REGION = "us-east-1";
    private static final String BUCKET = "YOUR BUCKET";
    private static final String KEY = "output/trades.csv";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem()
                .useProfileCredentialsProvider(PROFILE)
                .setRegion(REGION);
        s3.open();
        try {
            DataReader reader = new CSVReader(new InputStreamReader(s3.readFile(BUCKET, KEY)))
                    .setFieldNamesInFirstRow(true);
            DataWriter writer = StreamWriter.newSystemOutWriter();

            Job.run(reader, writer);
        } finally {
            s3.close();
        }
    }

}
