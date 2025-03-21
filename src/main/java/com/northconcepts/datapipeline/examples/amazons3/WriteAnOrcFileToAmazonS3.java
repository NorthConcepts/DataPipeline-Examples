package com.northconcepts.datapipeline.examples.amazons3;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataWriter;

public class WriteAnOrcFileToAmazonS3 {

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable {
        Path path = new Path("s3a://bucketName/output.orc");

        Configuration configuration = new Configuration();
        configuration.set("fs.s3a.access.key", ACCESS_KEY);
        configuration.set("fs.s3a.secret.key", SECRET_KEY);
        configuration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance.csv"))
                    .setFieldNamesInFirstRow(true);

        OrcDataWriter writer = new OrcDataWriter(path);

        Job.run(reader, writer);
    }
}
