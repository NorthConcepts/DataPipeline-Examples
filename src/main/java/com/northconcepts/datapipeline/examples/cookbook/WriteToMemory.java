/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryWriter;

public class WriteToMemory {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);

        MemoryWriter memoryWriter = new  MemoryWriter();
    
        Job.run(reader, memoryWriter);
        
        RecordList recordList = memoryWriter.getRecordList();
        for (int i = 0; i < recordList.getRecordCount(); i++) {
            log.info(recordList.get(i));
        }
    }

}
