/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.blog;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class MultipleExcelSheets {

    private static final String OUTPUT_FILE = "example/data/output/population.xlsx";

    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument();

        DataReader reader;
        DataWriter writer;
        
        reader = new MemoryReader(getCanadianProvinces());
        writer = new ExcelWriter(document).setSheetName("Canadian Provinces").setAutofitColumns(true);
        Job.run(reader, writer);
        
        reader = new MemoryReader(getUsStates());
        writer = new ExcelWriter(document).setSheetName("US States").setAutofitColumns(true);
        Job.run(reader, writer);

        document.save(new File(OUTPUT_FILE));
    }

    private static RecordList getCanadianProvinces() {
        RecordList list = new RecordList();
        list.add(new Record()
            .setField("Province", "Ontario")
            .setField("Population", 12851821)
            .setField("Population Growth", 5.7));
        
        list.add(new Record()
            .setField("Province", "Quebec")
            .setField("Population", 7903001)
            .setField("Population Growth", 4.7));
        
        list.add(new Record()
            .setField("Province", "British Columbia")
            .setField("Population", 4400057)
            .setField("Population Growth", 13.1));
        
        list.add(new Record()
            .setField("Province", "Alberta")
            .setField("Population", 3645257)
            .setField("Population Growth", 10.9));
        
        list.add(new Record()
            .setField("Province", "Manitoba")
            .setField("Population", 1208268)
            .setField("Population Growth", 3.6));
        
        return list;
    }

    private static RecordList getUsStates() {
        RecordList list = new RecordList();
        list.add(new Record()
            .setField("State", "California")
            .setField("Population", 38802500)
            .setField("Population Growth", 9.99));
        
        list.add(new Record()
            .setField("State", "Texas")
            .setField("Population", 26956958)
            .setField("Population Growth", 20.59));
        
        list.add(new Record()
            .setField("State", "Florida")
            .setField("Population", 19893297)
            .setField("Population Growth", 17.64));
        
        list.add(new Record()
            .setField("State", "New York")
            .setField("Population", 19746227)
            .setField("Population Growth", 2.12));
        
        list.add(new Record()
            .setField("State", "Illinois")
            .setField("Population", 12880580)
            .setField("Population Growth", 3.31));
        
        return list;
    }

}
