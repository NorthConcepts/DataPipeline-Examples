package com.northconcepts.datapipeline.examples.amazons3;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.amazons3.S3ObjectListing;
import com.northconcepts.datapipeline.amazons3.S3ObjectSummary;

public class ListAnAmazonS3FolderOneLevelAtATime {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";
    private static final String BUCKET = "YOUR BUCKET";
    private static final String FOLDER = "output/";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem()
                .setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY)
                .setDelimiter(AmazonS3FileSystem.FOLDER_DELIMITER);
        s3.open();
        try {
            S3ObjectListing listing = s3.listFolder(BUCKET, FOLDER);
            while (true) {
                for (String prefix : listing.getCommonPrefixes()) {
                    System.out.println("folder  " + prefix);
                }
                for (S3ObjectSummary object : listing.getObjectSummaries()) {
                    System.out.println("object  " + object.getKey() + "  " + object.getSize() + " bytes");
                }
                if (!listing.isTruncated()) {
                    break;
                }
                listing = s3.nextBatch(listing);
            }
        } finally {
            s3.close();
        }
    }

}
