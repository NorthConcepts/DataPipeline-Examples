package com.northconcepts.datapipeline.examples;

import com.northconcepts.datapipeline.avro.AvroReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.internal.test.FailingReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.retry.RetryStrategy;
import com.northconcepts.datapipeline.retry.RetryingOperation;

import java.io.File;
import java.io.IOException;

public class RetryOperationWithRetryCondition {


    public static void main(String[] args) throws Throwable {
        RetryingOperation<Void> retryingOperation = new RetryingOperation<>();
        retryingOperation.setInitialRetryDelay(1000L);
        retryingOperation.setMaxRetryCount(3);
        retryingOperation.setStrategy(RetryStrategy.EXPONENTIAL_BACKOFF);
        retryingOperation.setRetryPredicate(context -> context.getLastException() instanceof IOException);

        retryingOperation.call(() -> {
            DataReader reader = new AvroReader(new File("example/data/input/twitter.avro"));

            DataWriter writer = new CSVWriter(new File("example/data/output/twitter-out.csv"));

            Job.run(reader, writer);
            return null;
        });
    }
}
