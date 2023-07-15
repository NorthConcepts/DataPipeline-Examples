package com.northconcepts.datapipeline.foundations.examples.eventbus;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.eventbus.EventBus;
import com.northconcepts.datapipeline.eventbus.EventBusReader;
import com.northconcepts.datapipeline.eventbus.EventBusWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.SimpleXmlWriter;

public class UseAnEventBusInAPipeline {

    public static void main(String[] args) throws Throwable {
        DataReader reader ;
        DataWriter writer;

        final String PURCHASES_TOPIC = "purchases";

        EventBus bus = new EventBus().setName("App1 Bus");

        // read bus, write purchases to console
        reader = new EventBusReader(bus, PURCHASES_TOPIC);
        writer = new StreamWriter(System.out);
        Job.runAsync(reader, writer);

        // read bus, write purchases to file
        reader = new EventBusReader(bus, PURCHASES_TOPIC);
        writer = new SimpleXmlWriter(new File("example/data/output/purchases.xml"))
                .setPretty(true);
        Job.runAsync(reader, writer);

        // write purchases to bus
        reader = new CSVReader(new File("example/data/input/purchases.csv"))
                .setFieldNamesInFirstRow(true);
        writer = new EventBusWriter(bus, PURCHASES_TOPIC);
        Job.run(reader, writer);

        bus.shutdown();
    }
}
