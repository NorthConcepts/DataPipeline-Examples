/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.PipedReader;
import com.northconcepts.datapipeline.core.PipedWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;

public class PipeAWriterToAReader {
    
    public static void main(String[] args) {
        RecordProducer producer = new RecordProducer();
        producer.start();
        
        DataReader reader = producer.getReader();
        DataWriter writer = new StreamWriter(System.out);        
        
        Job.run(reader, writer);
    }


    public static class RecordProducer extends Thread {
        
        private final PipedReader reader = new PipedReader();
        private final PipedWriter writer = new PipedWriter(reader);

        public RecordProducer() {
        }
        
        public PipedReader getReader() {
            return reader;
        }
        
        @Override
        public void run() {
            writer.open();
            try {
                for (int i = 0; i < 20; i++) {
                    Record r = new Record();
                    r.setField("key", "key-" + i);
                    r.setField("value", "value-" + i);
                    writer.write(r);
                }
            } finally {
                writer.close();
            }
        }
        
    }
    
}
