/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontable;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableOutcome;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;

public class SaveAndLoadDecisionTableToXML {

    public static void main(String[] args) throws Throwable {
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
        
        //Save this DecisionTable to XML File.
        table.toXml(new FileWriter("example/data/output/decision-tree.xml"), true);
        
        DecisionTable decisionTable = new DecisionTable()
                .fromXml(new FileInputStream("example/data/output/decision-tree.xml"));
        
        System.out.println("DecisionTable loaded from XML:-\n" + decisionTable);
    }
}
