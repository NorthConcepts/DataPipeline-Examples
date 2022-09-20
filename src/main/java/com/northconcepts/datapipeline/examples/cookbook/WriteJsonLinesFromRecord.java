package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.json.JsonLinesWriter;

import java.io.File;

public class WriteJsonLinesFromRecord {
    public static void main(String[] args) {
        Record record1 = new Record();
        record1.setField("stageName", "John Wayne");
        record1.setField("realName", "Marion Robert Morrison");
        record1.setField("gender", "male");
        record1.setField("city", "Winterset");
        record1.setField("balance", 156.35);

        Record record2 = new Record();
        record2.setField("stageName", "Spiderman");
        record2.setField("realName", "Peter Parker");
        record2.setField("gender","male");
        record2.setField("city", "New York");
        record2.setField("balance", -0.96);

        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));
        DataWriter writer = new JsonLinesWriter(new File("data/output/json-lines-from-record.jsonl"))
                .setPretty(true);

        Job.run(reader, writer);
    }
}
