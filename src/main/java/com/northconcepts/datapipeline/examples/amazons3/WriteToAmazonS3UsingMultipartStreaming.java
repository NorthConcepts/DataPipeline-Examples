package com.northconcepts.datapipeline.examples.amazons3;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteToAmazonS3UsingMultipartStreaming {
    
    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
//        s3.setDebug(true);
        s3.open();
        try {
            // Create AWS S3 streaming, multi-part OutputStream 
            OutputStream outputStream = s3.writeMultipartFile("datapipeline-test-01", "output/trades.csv");

            DataReader reader = new CSVReader(new File("example/data/input/trades.csv"))
                    .setFieldNamesInFirstRow(true);
                
            DataWriter writer = new CSVWriter(new OutputStreamWriter(outputStream, "utf-8"))
                    .setFieldNamesInFirstRow(true);
            
            Job.run(reader, writer);
            
            System.out.println("Done.");
        } finally {
            s3.close();
        }
    }

}
