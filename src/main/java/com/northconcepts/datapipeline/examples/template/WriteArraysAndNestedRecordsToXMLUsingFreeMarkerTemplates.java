/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.template;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.template.TemplateWriter;

import java.io.File;
import java.io.FileWriter;

public class WriteArraysAndNestedRecordsToXMLUsingFreeMarkerTemplates {


    private static DataReader reader;

    public static void main(String... args) throws Throwable{
        createRecords();

        TemplateWriter writer = new TemplateWriter(new FileWriter("example/data/output/template-output.xml"));
        writer.setFieldNamesInFirstRow(false);
        writer.getConfiguration().setDirectoryForTemplateLoading(new File("example/data/input/template"));
        writer.setHeaderTemplate("header.xml");
        writer.setFooterTemplate("footer.xml");
        writer.setDetailTemplate("detail.xml");

        Job.run(reader, writer);
    }

    private static void createRecords() {
        Record home = new Record();
        home.getField("home", true).setValue("418-591-6446");

        Record office = new Record();
        office.getField("office", true).setValue("306-612-8887");

        Record address1 = new Record();
        address1.getField("street", true).setValue("1495  Lake City Way");
        address1.getField("city", true).setValue("Burnaby");
        address1.getField("province", true).setValue("British Columbia");
        address1.getField("zipCode", true).setValue("V5A 2Z6");
        address1.getField("contactNumber", true).setValue(home); // nested record

        Record address2 = new Record();
        address2.getField("street", true).setValue("4599  rue des Champs");
        address2.getField("city", true).setValue("Chicoutimi");
        address2.getField("province", true).setValue("Quebec");
        address2.getField("zipCode", true).setValue("G7H 4N3");
        address2.getField("contactNumber", true).setValue(office); // nested record

        ArrayValue arrayValue = new ArrayValue();
        arrayValue.addValue(address1);
        arrayValue.addValue(address2);

        Record record1 = new Record();
        record1.getField("stageName", true).setValue("John Wayne");
        record1.getField("realName", true).setValue("Marion Robert Morrison");
        record1.getField("gender", true).setValue("male");
        record1.getField("city", true).setValue("Winterset");
        record1.getField("balance", true).setValue(156.35);
        record1.getField("alternateAddress", true).setValue(arrayValue);

        Record record2 = new Record();
        record2.getField("stageName", true).setValue("Spiderman");
        record2.getField("realName", true).setValue("Peter Parker");
        record2.getField("gender", true).setValue("male");
        record2.getField("city", true).setValue("New York");
        record2.getField("balance", true).setValue(-0.96);
        record2.getField("alternateAddress", true).setValue(arrayValue.clone());

        reader = new MemoryReader(new RecordList(record1, record2));
    }
}
