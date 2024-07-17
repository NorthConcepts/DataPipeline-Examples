package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.internal.xpath.XmlNodeReader;
import com.northconcepts.datapipeline.xml.XmlReader;

public class ReadAnXmlFileWithByteOrderMark {
    
    public static final Logger log = DataEndpoint.log;

    public static void main(String[] args) throws Throwable {
        BOMInputStream bomInputStream = BOMInputStream.builder()
                .setInputStream(new FileInputStream(new File("example/data/input/xml_with_byte_order_marks.xml")))
                .setByteOrderMarks(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE,
                        ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE)
                .setInclude(false)
                .get();
        
        XMLStreamReader streamReader = Util.newXMLInputFactory().createXMLStreamReader(bomInputStream);
        DataReader reader = new XmlReader(new XmlNodeReader(streamReader))
        	.addField("title", "//book/title/text()")
        	.addField("language", "//book/title/@lang")
        	.addField("price", "//book/price/text()")
        	.addRecordBreak("//book");

        reader.open();
        try {
            Record record;
            while ((record = reader.read()) != null) {
                log.info(record);
                System.out.println(record);
            }
        } finally {
            reader.close();
        }
    }

}
