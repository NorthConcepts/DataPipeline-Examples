/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontable;


import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableReader;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class ChainDecisionTablesInPipeline {

    public static void main(String[] args) {

        DataReader reader = new MemoryReader(new RecordList(
                new Record().setField("sku", "S001").setField("price", 10.0).setField("quantity", 3),
                new Record().setField("sku", "S002").setField("price", 5000.0).setField("quantity", 10),
                new Record().setField("sku", "S003").setField("price", 0.12).setField("quantity", 50000)));

        reader = new DecisionTableReader(reader, new DecisionTable()
                .setName("Discount Table")
                .addField(new CalculatedField("total", "price * quantity"))

                .addRule(new DecisionTableRule()
                        .addCondition("quantity", "quantity > 10")
                        .addOutcome("discountPercentage", "10")
                        .addOutcome("discountAmount", "total * discountPercentage / 100.0")
                        .addOutcome("total", "total")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("total", "total > 10000")
                        .addOutcome("discountPercentage", "15")
                        .addOutcome("discountAmount", "total * discountPercentage / 100.0")
                        .addOutcome("total", "total - discountAmount")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("total", "total <= 10000")
                        .addOutcome("discountPercentage", "0")
                        .addOutcome("discountAmount", "0")
                        .addOutcome("total", "total")
                        )
                );

        reader = new DecisionTableReader(reader, new DecisionTable()
                .setName("Tax Table")

                .addRule(new DecisionTableRule()
                        .addCondition("total", "total <= 1000")
                        .addOutcome("tax", "total * 0.0")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("total", "total > 1000")
                        .addOutcome("tax", "total * 0.02")
                        )
                );


        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);

    }

}
