/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeReader;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddADecisionTreeToAPipeline {

    public static void main(String[] args) {

        DecisionTree tree = new DecisionTree()
            .addField(new CalculatedField("Variant Price", "toBigDecimal(${Variant Price})"))

            .setRootNode(new DecisionTreeNode()

                .addNode(new DecisionTreeNode("${Variant Price} == null || ${Variant Price} < 20")
                    .addOutcome("Shipping", "0.00")
                    .addOutcome("Total", "${Variant Price} + Shipping"))

                .addNode(new DecisionTreeNode("${Variant Price} < 50")
                    .addOutcome("Shipping", "5.00")
                    .addOutcome("Total", "${Variant Price} + Shipping"))

                .addNode(new DecisionTreeNode("${Variant Price} < 100")
                    .addOutcome("Shipping", "7.00")
                    .addOutcome("Total", "${Variant Price} + Shipping"))

                .addNode(new DecisionTreeNode("${Variant Price} >= 100")
                    .addOutcome("Shipping", "${Variant Price} * 0.10")
                    .addOutcome("Total", "${Variant Price} + Shipping")));

        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
            .setAllowMultiLineText(true)
            .setFieldNamesInFirstRow(true);

        reader = new DecisionTreeReader(reader, tree);

        reader = new TransformingReader(reader)
            .add(new SelectFields("Title", "Handle", "Variant Price", "Shipping", "Total"));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
