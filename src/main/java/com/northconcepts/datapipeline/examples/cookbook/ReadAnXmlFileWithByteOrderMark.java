package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlReader;

public class ReadAnXmlFileWithByteOrderMark {
    
    public static void main(String[] args) throws Throwable {
        BOMInputStream bomInputStream = BOMInputStream.builder()
                .setInputStream(new FileInputStream(new File("example/data/input/xml_with_byte_order_marks.xml")))
                .setByteOrderMarks(
                        ByteOrderMark.UTF_8,
                        ByteOrderMark.UTF_16BE,
                        ByteOrderMark.UTF_16LE,
                        ByteOrderMark.UTF_32BE,
                        ByteOrderMark.UTF_32LE
                        )
                .setInclude(false)
                .get();
        
        DataReader reader = new XmlReader(new InputStreamReader(bomInputStream))
        	.addField("title", "//book/title/text()")
        	.addField("language", "//book/title/@lang")
        	.addField("price", "//book/price/text()")
        	.addRecordBreak("//book");

        DataWriter writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
    }

}
