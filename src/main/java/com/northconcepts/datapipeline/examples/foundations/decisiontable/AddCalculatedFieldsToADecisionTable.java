/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.foundations.decisiontable;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableResult;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class AddCalculatedFieldsToADecisionTable {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Age", 49);
        input.setValue("House Owned", true);
        input.setValue("Income", 1000.0);
        
        DecisionTable table = new DecisionTable()
        		
                .addField("ageThreshold", "40")
                .addField("overAgeThreshold", "Age >= ageThreshold")
                
                .addRule(new DecisionTableRule()
                        .addCondition("overAgeThreshold", "? == true")
                        .addCondition("House Owned", "? == true")
                        .addOutcome("Eligible", "true")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("overAgeThreshold", "? == true")
                        .addCondition("House Owned", "? == false")
                        .addCondition("Income", "? >= 2000")
                        .addOutcome("Eligible", "true")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("overAgeThreshold", "? == true")
                        .addCondition("House Owned", "? == false")
                        .addCondition("Income", "? < 2000")
                        .addOutcome("Eligible", "false")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("overAgeThreshold", "? == false")
                        .addCondition("Income", "? >= 3000")
                        .addOutcome("Eligible", "true")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("overAgeThreshold", "? == false")
                        .addCondition("Income", "? < 3000")
                        .addOutcome("Eligible", "false")
                        )
                ;
        
        DecisionTableResult result = table.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }

}
