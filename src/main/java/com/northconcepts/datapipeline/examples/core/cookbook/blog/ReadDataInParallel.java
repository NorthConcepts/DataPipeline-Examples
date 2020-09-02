/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook.blog;

import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.junit.Test;

import com.northconcepts.datapipeline.core.AsyncMultiReader;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;

public class ReadDataInParallel {

    @Test
    public void readColours() throws Throwable {
        
        DataReader reader1 = null;
        DataReader reader2 = null;
        DataReader reader3 = null;
        
        DataReader reader = new AsyncMultiReader(reader1, reader2, reader3);
        DataWriter writer = new CSVWriter(new FileWriter("multi.csv")); 
        
        Job.run(reader, writer);
    }

    @Test
    public void readColours2() throws Throwable {
        
        DataReader reader = new AsyncMultiReader()
            .add(new SlowReader("orange", 5L * 1000L, 50L, false))
            .add(new SlowReader("red", 5L * 1000L, 50L, false))
            .add(new SlowReader("brown", 3L * 1000L, 40L, true))
            .add(new SlowReader("yellow", 2L * 1000L, 20L, true))
            .add(new SlowReader("yellow2", 2L * 1000L, 20L, true))
//            .setFailOnException(false)
            ;
        
        DataWriter writer = new CSVWriter(new OutputStreamWriter(System.out));
        
        try {
            Job.run(reader, writer);
            System.out.println("no exception.");
        } catch (Throwable e) {
//            e.printStackTrace();
            System.out.println("exception.");
        } 
    }

    
    @Test
    public void readColours3() throws Throwable {
        
        DataReader reader = new AsyncMultiReader()
            .add(new SlowReader("orange", 5L * 1000L, 50L, false))
            .add(new SlowReader("red", 5L * 1000L, 50L, false))
            .add(new SlowReader("brown", 3L * 1000L, 40L, true))
            .add(new SlowReader("yellow", 2L * 1000L, 20L, true))
            .add(new SlowReader("yellow2", 2L * 1000L, 20L, true))
            //.setFailOnException(false)
            ;
        
        DataWriter writer = new CSVWriter(new OutputStreamWriter(System.out));
        Job.run(reader, writer);
    }

    
    
    public static class SlowReader extends DataReader {
        
        private final String name;
        private final long elapsedTime;
        private final long recordDelay;
        private final boolean fail;
        
        public SlowReader(String name, long elapsedTime, long recordDelay, boolean fail) {
            this.name = name;
            this.elapsedTime = elapsedTime;
            this.recordDelay = recordDelay;
            this.fail = fail;
        }
        
        @Override
        protected Record readImpl() throws Throwable {
            if (getOpenElapsedTime() >= elapsedTime) {
                if (fail) {
                    throw exception("forced to fail");
                }
                return null;
            }
            
            Thread.sleep(recordDelay);
            
            Record record = new Record();
            record.setField("id", getRecordCount() + 1);
            record.setField("time", new Date());
            record.setField("name", name + "-" + getRecordCount());
            record.setField("overflow", Util.repeat("-", 8 * 1024));  // overflow I/O buffers, hopefully
            
            return record;
        }
        
        @Override
        public DataException addExceptionProperties(DataException exception) {
            exception.set("SlowReader.name", name);
            exception.set("SlowReader.elapsedTime", elapsedTime);
            exception.set("SlowReader.recordDelay", recordDelay);
            exception.set("SlowReader.fail", fail);
            return super.addExceptionProperties(exception);
        }
        
    }
    
}
