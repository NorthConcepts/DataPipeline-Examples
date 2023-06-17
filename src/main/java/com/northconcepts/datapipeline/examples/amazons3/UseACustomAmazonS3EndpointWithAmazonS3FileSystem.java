package com.northconcepts.datapipeline.examples.amazons3;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class UseACustomAmazonS3EndpointWithAmazonS3FileSystem {

	private static final String ACCESS_KEY = "YOUR ACCESS KEY";
	private static final String SECRET_KEY = "YOUR SECRET KEY";

	private static final String AWS_S3_CUSTOM_ENDPOINT = "YOUR S3 CUSTOM ENDPOINT";
	private static final String REGION = "YOUR AWS S3 REGION";

	public static void main(String[] args) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		AmazonS3FileSystem s3 = new AmazonS3FileSystem()
				.setCredentialsProvider(new AWSStaticCredentialsProvider(credentials))
				.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(AWS_S3_CUSTOM_ENDPOINT, REGION))
				.setDebug(true);

		s3.open();

		try {
			InputStream inputStream = s3.readFile("datapipeline-test-01", "output/trades.csv");

			DataReader reader = new CSVReader(new InputStreamReader(inputStream));
			DataWriter writer = new NullWriter();

			Job.run(reader, writer);

			System.out.println("Records read: " + writer.getRecordCount());
		} finally {
			s3.close();
		}
	}
}
