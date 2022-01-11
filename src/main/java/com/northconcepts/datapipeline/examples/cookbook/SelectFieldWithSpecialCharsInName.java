/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class SelectFieldWithSpecialCharsInName {

    public static void main(String[] args) {
        DataReader reader = new MemoryReader()
                .add(new Record()
                        .setField("ID", 123)
                        .setField("Date .(dd-MMM-yyyy", "13-Jan-2021")
                        .setField("Rate", 7.89)
                        .setField("Hours", 10)
                        );
        reader = new TransformingReader(reader)
                .add(new SelectFields("Hours", "Rate", "`Date .(dd-MMM-yyyy`"));

        DataWriter writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);

    }

}
