package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlReader;

import java.io.File;

public class ReadAnXmlFileUsingWildcard {

    public static void main(String[] args) {
        DataReader reader = new XmlReader(new File("example/data/input/products.xml"))
                .addRecordBreak("//product")
                .addField("Id", "//product/id")
                .addField("Name", "//product/name")
                .addField("Price", "//product/price_*") // This location path will match all nodes that starts with price_
                .addField("Currency", "//product/currency")
                ;
        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
