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
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Tree;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlReader;
import com.northconcepts.datapipeline.xml.XmlReader.DuplicateFieldPolicy;

public class DynamicallyMapXmlToCSV {

    public static void main(String[] args) {
        File inputFile = new File("example/data/input/call-center-agents.xml");

        DataReader reader = new XmlReader(inputFile)
                .setDuplicateFieldPolicy(DuplicateFieldPolicy.USE_LAST_VALUE)
                .setAddTextToParent(false)
                .setIgnoreNamespaces(true)
                .setAutoCloseReader(true)
                .setSaveLineage(false);

        Tree tree = Tree.loadXml(inputFile);

        // Add all fields
        tree.getAllFields()
        .forEach(a -> ((XmlReader) reader).addField(a.getName(), a.getXpathExpression()));

        // Add all record breaks
        tree.getAllRecordBreaks()
        .forEach(a -> ((XmlReader) reader).addRecordBreak(a.getXpathExpression()));

        DataWriter writer = new CSVWriter(new File("example/data/output/DynamicallyMapXmlToCSV_Output.csv"))
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
