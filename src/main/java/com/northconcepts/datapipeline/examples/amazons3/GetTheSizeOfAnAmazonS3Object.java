package com.northconcepts.datapipeline.examples.amazons3;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;

public class GetTheSizeOfAnAmazonS3Object {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";
    private static final String BUCKET = "YOUR BUCKET";
    private static final String KEY = "output/trades.csv";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem()
                .setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3.open();
        try {
            if (s3.exists(BUCKET, KEY)) {
                long size = s3.getFileSize(BUCKET, KEY);
                System.out.println(KEY + " is " + size + " bytes");
            } else {
                System.out.println(KEY + " does not exist");
            }
        } finally {
            s3.close();
        }
    }

}
