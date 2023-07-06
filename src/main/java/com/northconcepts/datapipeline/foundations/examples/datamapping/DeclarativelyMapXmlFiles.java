package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;
import java.io.FileInputStream;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlRecordReader;

public class DeclarativelyMapXmlFiles {
    
    public static void main(String[] args) throws Throwable {
//        DataReader reader = new XmlReader(new File("example/data/input/bookstore.xml"))
//                .addField("title", "//book/title/text()")
//                .addField("language", "//book/title/@lang")
//                .addField("price", "//book/price/text()")
//                .addRecordBreak("//book");
        
        DataReader reader = new XmlRecordReader(new File("example/data/input/bookstore.xml"))
                .addRecordBreak("//book");

        reader = new DataMappingReader(reader, new DataMapping()
                .fromXml(new FileInputStream(new File("example/data/input/datamapping/book-mapping.xml"))));

        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
        
    }

}
