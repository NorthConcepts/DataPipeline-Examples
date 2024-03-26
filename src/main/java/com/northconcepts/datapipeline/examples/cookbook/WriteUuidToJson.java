package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.util.UUID;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonWriter;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteUuidToJson {

    public static void main(String[] args) {
        Record record1 = new Record();
        record1.setField("contact_id", UUID.fromString("54e2037c-13c5-42fd-af89-97facd8bfdb8"));
        record1.setField("first_name", "John");
        record1.setField("last_name", "Doe");
 
        Record record2 = new Record();
        record2.setField("contact_id", UUID.fromString("03199f44-943e-49c3-9f56-b876eaeabba5"));
        record2.setField("first_name", "Jane");
        record2.setField("last_name", "Doe");
                
        DataReader reader = new MemoryReader(new RecordList(record1, record2));
         
        DataWriter writer = new SimpleJsonWriter(new File("example/data/output/write-uuid-to-json.json"))
                .setPretty(true);
         
        Job.run(reader, writer);
    }
}
