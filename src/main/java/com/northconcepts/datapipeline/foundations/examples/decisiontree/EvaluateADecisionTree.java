package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class EvaluateADecisionTree {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Age", 49);
        input.setValue("houseOwned", true);
        input.setValue("Income", 1000.0);

        DecisionTree tree = new DecisionTree()
                .setRootNode(new DecisionTreeNode()

                        .addNode(new DecisionTreeNode()
                                .setCondition("Age >= 40")
                                .addNode(new DecisionTreeNode()
                                        .setCondition("houseOwned == true")
                                        .addOutcome("Eligible", "true")
                                        )

                                .addNode(new DecisionTreeNode()
                                        .setCondition("houseOwned == false")
                                        .addNode(new DecisionTreeNode()
                                                .setCondition("Income >= 2000")
                                                .addOutcome("Eligible", "true")
                                                )
                                        .addNode(new DecisionTreeNode()
                                                .setCondition("Income < 2000")
                                                .addOutcome("Eligible", "false")
                                                )
                                )
                        )

                        .addNode(new DecisionTreeNode()
                                .setCondition("Age < 40")
                                .addNode(new DecisionTreeNode()
                                        .setCondition("Income >= 3000")
                                        .addOutcome("Eligible", "true")
                                        )
                                .addNode(new DecisionTreeNode()
                                        .setCondition("Income < 3000")
                                        .addOutcome("Eligible", "false")
                                        )
                        )
                );

        DecisionTreeResult result = tree.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }
}
