/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.OutputStreamWriter;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonWriter;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteToJsonStream {

    /*
     * Produces the following JSON (line breaks added for clarity)
     * 
     * [ { "stageName":"John Wayne", "realName":"Marion Robert Morrison", "gender":"male", "city":"Winterset", "balance":156.35 }, {
     * "stageName":"Spiderman", "realName":"Peter Parker", "gender":"male", "city":"New York", "balance":-0.96 } ]
     */

    public static void main(String[] args) throws Throwable {

        Record record1 = new Record();
        record1.setField("stageName", "John Wayne");
        record1.setField("realName", "Marion Robert Morrison");
        record1.setField("gender", "male");
        record1.setField("city", "Winterset");
        record1.setField("balance", 156.35);

        Record record2 = new Record();
        record2.setField("stageName", "Spiderman");
        record2.setField("realName", "Peter Parker");
        record2.setField("gender","male");
        record2.setField("city", "New York");
        record2.setField("balance", -0.96);

        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));

        SimpleJsonWriter writer = new SimpleJsonWriter(new OutputStreamWriter(System.out));

        Job.run(reader, writer);
    }

}
