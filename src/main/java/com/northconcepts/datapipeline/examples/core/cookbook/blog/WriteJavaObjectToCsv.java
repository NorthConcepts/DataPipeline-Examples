/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook.blog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.examples.cookbook.blog.Signal;
import com.northconcepts.datapipeline.javabean.JavaBeanReader;
import com.northconcepts.datapipeline.job.Job;

public class WriteJavaObjectToCsv {

    public static void main(String[] args) {

        List<Signal> messages = getTestData();

        JavaBeanReader reader = new JavaBeanReader("messages", messages);

        reader.addField("source", "//source/text()");
        reader.addField("name", "//name/text()");
        reader.addField("component", "//component/text()");
        reader.addField("occurrence", "//occurrence/text()");
        reader.addField("size", "//size/text()");
        reader.addField("bandwidth", "//bandwidth/text()");
        reader.addField("sizewithHeader", "//sizewithHeader/text()");
        reader.addField("bandwidthWithHeader", "//bandwidthWithHeader/text()");

        reader.addRecordBreak("//Signal");

        DataWriter writer = new CSVWriter(new File("example/data/output/output.csv"));

        Job.run(reader, writer);
    }

    private static List<Signal> getTestData() {
        List<Signal> messages = new ArrayList<Signal>();

        messages.add(new Signal("TestSource1", "TestName1", "TestComponent1", 11, 12, 1.3f, 14, 1.5f));
        messages.add(new Signal("TestSource2", "TestName2", "TestComponent2", 21, 22, 2.3f, 24, 2.5f));

        return messages;
    }

}
