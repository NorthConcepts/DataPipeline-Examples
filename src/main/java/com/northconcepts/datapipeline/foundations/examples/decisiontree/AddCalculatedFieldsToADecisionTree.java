/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class AddCalculatedFieldsToADecisionTree {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Age", 49);
        input.setValue("houseOwned", true);
        input.setValue("Income", 1000.0);

        DecisionTree tree = new DecisionTree()

            .addField("ageThreshold", "40")
            .addField("overAgeThreshold", "Age >= ageThreshold")

            .setRootNode(new DecisionTreeNode()

                .addNode(new DecisionTreeNode("overAgeThreshold == true")
                    .addNode(new DecisionTreeNode("houseOwned == true").addOutcome("Eligible", "true"))

                    .addNode(new DecisionTreeNode().setCondition("houseOwned == false")
                        .addNode(new DecisionTreeNode("Income >= 2000").addOutcome("Eligible", "true"))
                        .addNode(new DecisionTreeNode("Income < 2000").addOutcome("Eligible", "false"))))

                .addNode(new DecisionTreeNode().setCondition("overAgeThreshold == false")
                    .addNode(new DecisionTreeNode("Income >= 3000").addOutcome("Eligible", "true"))
                    .addNode(new DecisionTreeNode("Income < 3000").addOutcome("Eligible", "false"))));

        DecisionTreeResult result = tree.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }
}
