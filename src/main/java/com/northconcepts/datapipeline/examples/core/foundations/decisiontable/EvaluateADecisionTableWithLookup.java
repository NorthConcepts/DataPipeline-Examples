/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontable;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableResult;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableRule;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

public class EvaluateADecisionTableWithLookup {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Price", 159);
        input.setValue("Currency Code", "cad");

        Lookup currencyLookup = new BasicLookup(new FieldList("Currency Name"))
                .add("CAD", "Canadian Dollars")
                .add("USD", "American Dollars")
                .add("EUR", "Euros")
                .add("GBP", "British Pounds")
                .add("MXN", "Mexican Pesos")
                ;
        
        DecisionTable table = new DecisionTable()
                .setValue("currencyLookup", currencyLookup)
                .addRule(new DecisionTableRule()
                        .addCondition("Price", "? == null || ? < 20")
                        .addOutcome("Shipping", "0.00")
                        .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code})")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("Price", "? < 50")
                        .addOutcome("Shipping", "5.00")
                        .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code})")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("Price", "? < 100")
                        .addOutcome("Shipping", "7.00")
                        .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code})")
                        )
                .addRule(new DecisionTableRule()
                        .addCondition("Price", "? >= 100")
                        .addOutcome("Shipping", "${Price} * 0.10")
                        .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code})")
                        )
                ;
        
        DecisionTableResult result = table.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }

}
