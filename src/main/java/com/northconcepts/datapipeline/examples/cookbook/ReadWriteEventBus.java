/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.eventbus.EventBus;
import com.northconcepts.datapipeline.eventbus.EventBusReader;
import com.northconcepts.datapipeline.eventbus.EventBusWriter;
import com.northconcepts.datapipeline.job.Job;

public class ReadWriteEventBus {

    public static void main(String[] args) throws Throwable {
        DataReader reader;
        DataWriter writer;

        final String PURCHASES_TOPIC = "purchases";

        EventBus bus = new EventBus().setName("Purchases Bus");
        
        // read purchases from bus, write purchases to console
        reader = new EventBusReader(bus, PURCHASES_TOPIC); 
        writer = new StreamWriter(System.out);
        Job.runAsync(reader, writer);
        
        // read CSV file, write purchases to bus (for above reader to consume)
        reader = new CSVReader(new File("example/data/input/purchases.csv"))
                .setFieldNamesInFirstRow(true);
        writer = new EventBusWriter(bus, PURCHASES_TOPIC);
        Job.run(reader, writer);

        bus.shutdown();
    }
}
