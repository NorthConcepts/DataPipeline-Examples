/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.amazons3;

import java.io.File;
import java.io.OutputStream;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteExcelToAmazonS3 {
    
    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        AmazonS3FileSystem s3 = new AmazonS3FileSystem();
        s3.setBasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3.open();

        try {
            OutputStream outputStream = s3.writeMultipartFile("datapipeline-test-01", "output/call-center-inbound-call.xlsx");

            ExcelDocument document = new ExcelDocument(ExcelDocument.ProviderType.POI_XSSF_SAX)
                    .open(new File("example/data/input/call-center-inbound-call.xlsx"));

            DataReader reader = new ExcelReader(document)
                    .setFieldNamesInFirstRow(true);

            DataWriter writer = new ExcelWriter(document).setSheetName("inbound-calls");

            Job.run(reader, writer);

            document.save(outputStream);
        } finally {
            s3.close();
        }
    }

}
