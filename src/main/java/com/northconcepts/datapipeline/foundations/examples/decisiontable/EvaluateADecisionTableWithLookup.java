package com.northconcepts.datapipeline.foundations.examples.decisiontable;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTable;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableCondition;
import com.northconcepts.datapipeline.foundations.decisiontable.DecisionTableOutcome;
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
                        .addCondition(new DecisionTableCondition("Price", "? == null || ? < 20"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "0.00"))
                        .addOutcome(new DecisionTableOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Price", "? < 50"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "5.00"))
                        .addOutcome(new DecisionTableOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Price", "? < 100"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "7.00"))
                        .addOutcome(new DecisionTableOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
                        )
                .addRule(new DecisionTableRule()
                        .addCondition(new DecisionTableCondition("Price", "? >= 100"))
                        .addOutcome(new DecisionTableOutcome("Shipping", "${Price} * 0.10"))
                        .addOutcome(new DecisionTableOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
                        )
                ;
        
        DecisionTableResult result = table.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }

}
