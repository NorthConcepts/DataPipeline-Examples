package com.northconcepts.datapipeline.examples.cookbook;

import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.buffer.BufferStrategy;
import com.northconcepts.datapipeline.buffer.BufferedReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;


public class BufferRecordsByTimePeriodOrCount {

    private static final int MAX_TRUCKS = 10;
    private static final long MAX_PACKAGES = 200;
    private static final int RECORD_DELAY_MILLISECONDS = 250;

    public static void main(String[] args) {
        
        DataReader reader = new FakePackageReader(MAX_TRUCKS, MAX_PACKAGES, RECORD_DELAY_MILLISECONDS);
        
        // group records by "truck_id" and release all records for each "truck_id" downstream every 10 seconds or 
        // when 10 records for that "truck_id" have been collected
        // and limit the internal buffer size to 100 records
        
        reader = new BufferedReader(reader, 100, "truck_id")
                .setBufferStrategy(BufferStrategy.limitedTimeFromOpenOrLimitedRecords(TimeUnit.SECONDS.toMillis(10), 10))
//                .setBufferStrategy(BufferStrategy.limitedTimeFromLastUpdateOrLimitedRecords(10000, 10))
//                .setDebug(true)
                ;
        
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        
        System.out.println(job.getRecordsTransferred() + "  -  " + job.getRunningTimeAsString());
    }

    
    //==================================================
    
    public static class FakePackageReader extends DataReader {
        
        private final int maxTrucks;
        private final long maxPackages;
        private long nextPackageId;
        private final long recordDelay;
        
        public FakePackageReader(int maxTrucks, long maxPackages, long recordDelay) {
            this.maxTrucks = maxTrucks;
            this.maxPackages = maxPackages;
            this.recordDelay = recordDelay;
        }
        
        @Override
        protected Record readImpl() throws Throwable {
            if (nextPackageId >= maxPackages) {
                return null;
            }
            
            if (recordDelay > 0) {
                Thread.sleep(recordDelay);
            }
            
            Record record = new Record();
            record.setField("package_id", nextPackageId++);
            record.setField("truck_id", "truck" + nextPackageId % maxTrucks);
            record.setField("amount", nextPackageId + 0.01);
            return record;
        }
        
    }

}
