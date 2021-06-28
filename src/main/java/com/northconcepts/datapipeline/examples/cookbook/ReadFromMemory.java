/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class ReadFromMemory {

    public static void main(String[] args) {
        Record record1 = new Record();
        record1.setField("name", "John Wayne");
        record1.setField("balance", 156.35);

        Record record2 = new Record();
        record2.setField("name", "Peter Parker");
        record2.setField("balance", 0.96);
        
        RecordList recordList = new RecordList(record1, record2);
        
        DataReader reader = new MemoryReader(recordList);
        DataWriter writer = new CSVWriter(new File("example/data/output/credit-balance-02.csv"));
    
        Job.run(reader, writer);
    }
    
}
