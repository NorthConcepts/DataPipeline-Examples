package com.northconcepts.datapipeline.examples.kafka;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.kafka.KafkaReader;
import com.northconcepts.datapipeline.kafka.KafkaWriter;

import java.io.File;
import java.util.Properties;

public class ReadAndWriteKafkaEvents {

    private static String BOOTSTRAP_SERVERS = "localhost:9092,another.host:port";
    private static String GROUP_ID = "group_id";
    private static String TOPIC = "jewelry";

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
            .setAllowMultiLineText(true)
            .setFieldNamesInFirstRow(true);

        Properties writerProps = new Properties();
        writerProps.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        DataWriter writer = new KafkaWriter(writerProps, TOPIC);

        Job.run(reader, writer);

        Properties readerProps = new Properties();
        readerProps.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        readerProps.put("group.id", GROUP_ID);

        reader = new KafkaReader(readerProps, TOPIC, 500L).setKeepPolling(false);
        writer = new CSVWriter(new File("data/output/jewelry-events.csv"));

        Job.run(reader, writer);
    }
}
