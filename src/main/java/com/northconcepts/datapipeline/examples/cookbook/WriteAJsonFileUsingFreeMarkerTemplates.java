package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.template.TemplateWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteAJsonFileUsingFreeMarkerTemplates {
    public static void main(String[] args) throws IOException {
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

        RecordList recordList = new RecordList(record1, record2);
        DataReader reader = new MemoryReader(recordList);

        TemplateWriter writer = new TemplateWriter(new FileWriter("example/data/output/credit-balance-04.json"));
        writer.setFieldNamesInFirstRow(false);
        writer.getConfiguration().setDirectoryForTemplateLoading(new File("example/data/input"));
        writer.setHeaderTemplate("WriteAJsonFileUsingFreeMarkerTemplates-header.json");
        writer.setFooterTemplate("WriteAJsonFileUsingFreeMarkerTemplates-footer.json");
        writer.setDetailTemplate("WriteAJsonFileUsingFreeMarkerTemplates-detail.json");

        Job.run(reader, writer);
    }
}
