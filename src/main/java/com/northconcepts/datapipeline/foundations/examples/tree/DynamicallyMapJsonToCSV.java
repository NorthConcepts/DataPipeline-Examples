/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */

package com.northconcepts.datapipeline.foundations.examples.tree;

import java.io.File;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Tree;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;

public class DynamicallyMapJsonToCSV {

    public static void main(String[] args) {
        File inputFile = new File("example/data/input/pipeline/MOCK_DATA.json");

        JsonReader reader = new JsonReader(inputFile);

        Tree tree = Tree.loadJson(inputFile);

        // Add all fields
        tree.getAllFields()
        .forEach(treeNode -> reader.addField(treeNode.getName(), treeNode.getXpathExpression()));

        // Add all record breaks
        tree.getAllRecordBreaks()
        .forEach(treeNode -> reader.addRecordBreak(treeNode.getXpathExpression()));

        DataWriter writer = new CSVWriter(new File("example/data/output/DynamicallyMapJsonToCSV_Output.csv"));

        Job.run(reader, writer);
    }

}
