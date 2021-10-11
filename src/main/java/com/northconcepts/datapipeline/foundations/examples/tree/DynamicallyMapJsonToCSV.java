/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */

package com.northconcepts.datapipeline.foundations.examples.tree;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.csv.CSVWriter.ValuePolicy;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;
import com.northconcepts.datapipeline.xml.XmlReader.DuplicateFieldPolicy;

public class DynamicallyMapJsonToCSV {

    public static void main(String[] args) {
        DataReader reader = new JsonReader(new File("example/data/input/pipeline/MOCK_DATA.json"))
                .useBigDecimal(false)
                .setDuplicateFieldPolicy(DuplicateFieldPolicy.USE_LAST_VALUE)
                .setAddTextToParent(false)
                .setIgnoreNamespaces(true)
                .setAutoCloseReader(true)
                .addField("id", "/array/object/id", false)
                .addField("first_name", "/array/object/first_name", false)
                .addField("last_name", "/array/object/last_name", false)
                .addField("email", "/array/object/email", false)
                .addRecordBreak("/array/object")
                .setSaveLineage(false);

        DataWriter writer = new CSVWriter(new File("example/data/output/output.csv"))
                .setFieldSeparator(",")
                .setStartingQuote("\"")
                .setEndingQuote("\"")
                .setNullValuePolicy(ValuePolicy.EMPTY_STRING)
                .setEmptyStringValuePolicy(ValuePolicy.QUOTED_EMPTY_STRING)
                .setForceQuote(false)
                .setNewLine("\r\n")
                .setAutoCloseWriter(true)
                .setFlushOnWrite(false)
                .setFieldNamesInFirstRow(true);

        Job.run(reader, writer);
    }

}
