package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.AsyncReader;
import com.northconcepts.datapipeline.core.AsyncWriter;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.Transformer;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.TransformingWriter;

public class UseVirtualThreads {

    public static void main(String[] args) throws Throwable {
        System.out.println("---- Platform threads (default) ----");
        Job job = createJob();
        job.runAsync();
        job.waitUntilFinished();

        System.out.println("\n---- Virtual threads ----");
        job = createJob()
                .setThreadFactory(Thread.ofVirtual().factory());
        job.runAsync();
        // virtual threads are daemon threads, so without this wait the JVM can exit before the job finishes
        job.waitUntilFinished();
    }

    private static Job createJob() {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(Transformer.of(record -> {
                    System.out.println("Reading account " + record.getFieldValueAsString("Account", null) + " on " + Thread.currentThread());
                    return true;
                }));

        reader = new AsyncReader(reader);

        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-virtual-threads.csv"))
                .setFieldNamesInFirstRow(true);

        writer = new TransformingWriter(writer)
                .add(Transformer.of(record -> {
                    System.out.println("Writing account " + record.getFieldValueAsString("Account", null) + " on " + Thread.currentThread());
                    return true;
                }));

        writer = new AsyncWriter(writer);

        return new Job(reader, writer);
    }

}
