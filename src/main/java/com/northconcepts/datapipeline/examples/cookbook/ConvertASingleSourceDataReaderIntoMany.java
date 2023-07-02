package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.avro.AvroReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonWriter;
import com.northconcepts.datapipeline.multiplex.SplitWriter;
import com.northconcepts.datapipeline.xml.SimpleXmlWriter;

public class ConvertASingleSourceDataReaderIntoMany {

    public static void main(String[] args) {
        SplitWriter splitWriter = new SplitWriter();
        
        DataReader reader1 = splitWriter.createReader();
        DataReader reader2 = splitWriter.createReader();
        
        DataWriter target1 = new SimpleXmlWriter(new File("example/data/output/simple-xml-splitwriter.xml"))
                .setPretty(true);
        
        DataWriter target2 = new SimpleJsonWriter(new File("example/data/output/simple-json-splitwriter.json"))
                .setPretty(true);
        
        Job.runAsync(reader1, target1);
        Job.runAsync(reader2, target2);
        
        DataReader source = new AvroReader(new File("example/data/input/twitter.avro"));
        Job.run(source, splitWriter);
    }
}
