package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlRecordReader;

public class ReadXmlRecordsFromFile {

    public static void main(String[] args) {
        DataReader reader = new XmlRecordReader(new File("example/data/input/simple-xml-input.xml"))
                .addRecordBreak("/records/record");
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }
}
