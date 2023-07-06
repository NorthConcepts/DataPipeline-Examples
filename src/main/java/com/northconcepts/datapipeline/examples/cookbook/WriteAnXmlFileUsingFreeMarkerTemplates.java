package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.FileWriter;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.template.TemplateWriter;

public class WriteAnXmlFileUsingFreeMarkerTemplates {

    /*
     * Produces the following XML (line breaks added for clarity)
     * 
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?> <actors> <north-america> <actor stage-name="John Wayne" real-name=
     * "Marion Robert Morrison"> <gender>male</gender> <city>Winterset</city> <balance>156.35</balance> </actor> <actor
     * stage-name="Spiderman" real-name="Peter Parker"> <gender>male</gender> <city>New York</city> </actor> </north-america> </actors>
     * 
     */

    public static void main(String[] args) throws Throwable {

        Record record1 = new Record();
        record1.getField("stageName", true).setValue("John Wayne");
        record1.getField("realName", true).setValue("Marion Robert Morrison");
        record1.getField("gender", true).setValue("male");
        record1.getField("city", true).setValue("Winterset");
        record1.getField("balance", true).setValue(156.35);

        Record record2 = new Record();
        record2.getField("stageName", true).setValue("Spiderman");
        record2.getField("realName", true).setValue("Peter Parker");
        record2.getField("gender", true).setValue("male");
        record2.getField("city", true).setValue("New York");
        record2.getField("balance", true).setValue(-0.96);

        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));

        TemplateWriter writer = new TemplateWriter(new FileWriter("example/data/output/credit-balance-04.xml"));
        writer.setFieldNamesInFirstRow(false);
        writer.getConfiguration().setDirectoryForTemplateLoading(new File("example/data/input"));
        writer.setHeaderTemplate("WriteAnXmlFileUsingFreeMarkerTemplates-header.xml");
        writer.setFooterTemplate("WriteAnXmlFileUsingFreeMarkerTemplates-footer.xml");
        writer.setDetailTemplate("WriteAnXmlFileUsingFreeMarkerTemplates-detail.xml");

        Job.run(reader, writer);
    }

}
