package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

public class EvaluateADecisionTreeWithLookup {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Price", 159);
        input.setValue("Currency Code", "cad");

        Lookup currencyLookup = new BasicLookup(new FieldList("Currency Name"))
            .add("CAD", "Canadian Dollars")
            .add("USD", "American Dollars")
            .add("EUR", "Euros")
            .add("GBP", "British Pounds")
            .add("MXN", "Mexican Pesos");

        DecisionTree tree = new DecisionTree()

            .setValue("currencyLookup", currencyLookup)
            
            .setRootNode(new DecisionTreeNode()
                
                .addNode(new DecisionTreeNode("Price == null || Price < 20")
                    .addOutcome("Shipping", "0.00")
                    .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
    
                .addNode(new DecisionTreeNode("Price  < 50")
                    .addOutcome("Shipping", "5.00")
                    .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
    
                .addNode(new DecisionTreeNode("Price  < 100")
                    .addOutcome("Shipping", "7.00")
                    .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))"))
    
                .addNode(new DecisionTreeNode("Price  >= 100")
                    .addOutcome("Shipping", "${Price} * 0.10")
                    .addOutcome("Currency", "lookup(0, currencyLookup, toUpperCase(${Currency Code}))")));

        DecisionTreeResult result = tree.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }
}
