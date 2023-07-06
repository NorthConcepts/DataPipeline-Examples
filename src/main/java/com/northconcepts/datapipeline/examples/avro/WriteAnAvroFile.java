package com.northconcepts.datapipeline.examples.avro;

import java.io.File;
import java.util.Arrays;

import com.northconcepts.datapipeline.avro.AvroWriter;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteAnAvroFile {
    
    public static void main(String[] args) {
        DataReader reader = new MemoryReader(new RecordList(createRecord("One"), createRecord("Two"),
                createRecord("Three")));
        DataWriter writer = new AvroWriter(new File("example/data/output/bird-iq.avro"), null);
        
        Job.run(reader, writer);
    }
    
    private static Record createRecord(String title) {
        Record record = new Record();
        record.setField("Title", title);
        record.setField("Number", 3.14);
        record.setField("BirdIQ", Arrays.asList("b","i","r","d","i","q"));
        return record;
    }

}
