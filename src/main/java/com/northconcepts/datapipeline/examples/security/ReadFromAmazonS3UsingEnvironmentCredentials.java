package com.northconcepts.datapipeline.examples.security;

import java.io.InputStreamReader;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.security.Credentials;
import com.northconcepts.datapipeline.security.EnvironmentCredentialsResolver;

public class ReadFromAmazonS3UsingEnvironmentCredentials {

    private static final String BUCKET = "YOUR BUCKET";
    private static final String KEY = "output/trades.csv";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem()
                .setCredentialsResolver(new EnvironmentCredentialsResolver()
                        .map(Credentials.ACCESS_KEY, "AWS_ACCESS_KEY_ID")
                        .map(Credentials.SECRET_KEY, "AWS_SECRET_ACCESS_KEY")
                        .map(Credentials.SESSION_TOKEN, "AWS_SESSION_TOKEN"));
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
