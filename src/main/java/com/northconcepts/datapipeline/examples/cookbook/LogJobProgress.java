/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.job.JobCallback;

public class LogJobProgress {

    private static final long RUNNING_TIME = TimeUnit.SECONDS.toMillis(30);
    private static final long LOG_INTERVAL = TimeUnit.SECONDS.toMillis(5);

    public static void main(String[] args) {
        
        DataReader reader = new SlowReader("orange", RUNNING_TIME, 50L, false);
        DataWriter writer = new NullWriter();
        
        JobCallback<DataReader, DataWriter> jobCallback = new JobCallback<DataReader, DataWriter>(){
            
            private long nextLogTimeMillis;

            @Override
            public void onProgress(DataReader reader, DataWriter writer, Record currentRecord) throws Throwable {
                if (System.currentTimeMillis() > nextLogTimeMillis) {
                    nextLogTimeMillis = System.currentTimeMillis() + LOG_INTERVAL; 
                    System.out.println("Records: " + reader.getRecordCount());
                }
            }

            @Override
            public void onSuccess(DataReader reader, DataWriter writer) throws Throwable {
                System.out.println("DONE");
            }

            @Override
            public void onFailure(DataReader reader, DataWriter writer, Record currentRecord, Throwable exception) throws Throwable {
                System.out.println("FAILED: " + exception);
            }
            
        };
        
        Job.run(reader, writer, jobCallback);
        
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
