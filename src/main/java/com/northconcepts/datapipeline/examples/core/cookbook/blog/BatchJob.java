/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook.blog;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class BatchJob {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/trades.csv"))
                .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("price").stringToDouble());
        
        reader = new GroupByReader(reader, "stock")
                .first("price", "open")
                .last("price", "close")
                .min("price", "low")
                .max("price", "high");
        
//        reader = new LimitReader(reader, 10);
//        reader = new SortingReader(reader).asc("stock");
        
        DataWriter writer = new CSVWriter(new File("example/data/output/prices.csv"));
//        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);

    }

}
