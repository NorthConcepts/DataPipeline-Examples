/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableOutcome;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableReader;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class AddADecisionTableToAPipeline {

    public static void main(String[] args) {

        List<DecisionTableOutcome> outcomes = new ArrayList<>();
        outcomes.add(new DecisionTableOutcome("Total", "${Variant Price} + Shipping"));
        outcomes.add(new DecisionTableOutcome("Product Type", "Type"));
        
        DecisionTable table = new DecisionTable()
                .addField(new CalculatedField("Variant Price", "toBigDecimal(${Variant Price})"))
                
                .addRule(new DecisionTableRule()
                        .addCondition("Variant Price", "? == null || ? < 20")
                        .addOutcome("Shipping", "0.00")
                        .addOutcome(outcomes)
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("Variant Price", "? < 50")
                        .addOutcome("Shipping", "5.00") 
                        .addOutcome(outcomes)
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("Variant Price", "? < 100")
                        .addOutcome("Shipping", "7.00")
                        .addOutcome(outcomes)
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("Variant Price", "? >= 100")
                        .addOutcome(new DecisionTableOutcome("Shipping", "${Variant Price} * 0.10"))
                        .addOutcome(outcomes)
                        )
                ;
        
        
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);
        
        reader = new DecisionTableReader(reader, table);
        
        reader = new TransformingReader(reader).add(new SelectFields("Title", "Handle", "Variant Price", "Shipping", "Total", "Type"));
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
        
    }

}
