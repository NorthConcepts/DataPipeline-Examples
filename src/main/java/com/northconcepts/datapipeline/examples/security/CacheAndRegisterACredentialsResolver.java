package com.northconcepts.datapipeline.examples.security;

import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.amazons3.AmazonS3FileSystem;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.security.CachingCredentialsResolver;
import com.northconcepts.datapipeline.security.Credentials;
import com.northconcepts.datapipeline.security.CredentialsResolver;
import com.northconcepts.datapipeline.security.CredentialsResolverRegistry;
import com.northconcepts.datapipeline.security.SuppliedCredentialsResolver;

public class CacheAndRegisterACredentialsResolver {

    private static final String RESOLVER_ID = "s3-trades";
    private static final String BUCKET = "YOUR BUCKET";
    private static final String KEY = "output/trades.csv";

    public static void main(String[] args) throws Throwable {
        CredentialsResolver resolver = new CachingCredentialsResolver(
                new SuppliedCredentialsResolver(CacheAndRegisterACredentialsResolver::fetchCredentials),
                TimeUnit.MINUTES.toMillis(15));

        CredentialsResolverRegistry.getSystemRegistry().add(RESOLVER_ID, resolver);

        AmazonS3FileSystem s3 = new AmazonS3FileSystem()
                .setCredentialsResolver(CredentialsResolverRegistry.getSystemRegistry().require(RESOLVER_ID));
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

    private static Credentials fetchCredentials() {
        return Credentials.builder()
                .set(Credentials.ACCESS_KEY, "YOUR ACCESS KEY")
                .set(Credentials.SECRET_KEY, "YOUR SECRET KEY")
                .setExpiresOn(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1))
                .build();
    }

}
