package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.xml.SimpleXmlWriter;

public class WriteSimpleXmlFile {

    public static void main(String[] args) {
        Record record1 = new Record();
        record1.setField("country", "Philippines");
        record1.setField("capital", "Manila");
        record1.setField("language", "Filipino");
 
        Record record2 = new Record();
        record2.setField("country", "Japan");
        record2.setField("capital", "Tokyo");
        record2.setField("language", "Japanese");
                
        DataReader reader = new MemoryReader(new RecordList(record1, record2));
         
        DataWriter writer = new SimpleXmlWriter(new File("example/data/output/simple-xml-to-file.xml"))
                .setPretty(true);
         
        Job.run(reader, writer);

    }

}
