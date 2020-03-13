package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeOutcome;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeResult;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class EvaluateADecisionTree {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Age", 49);
        input.setValue("houseOwned", true);
        input.setValue("Income", 1000.0);

        DecisionTree tree = new DecisionTree()
                .setRootNode(new DecisionTreeNode()

                        .addBranch("Age >= 40", new DecisionTreeNode()
                                .addBranch("houseOwned == true", new DecisionTreeNode()
                                        .addOutcome(new DecisionTreeOutcome("Eligible", "true"))
                                )

                                .addBranch("houseOwned == false", new DecisionTreeNode()

                                        .addBranch("Income >= 2000", new DecisionTreeNode()
                                                .addOutcome(new DecisionTreeOutcome("Eligible", "true"))
                                        )

                                        .addBranch("Income < 2000", new DecisionTreeNode()
                                                .addOutcome(new DecisionTreeOutcome("Eligible", "false"))
                                        )

                                )
                        )

                        .addBranch("Age < 40", new DecisionTreeNode()
                                .addBranch("Income >= 3000", new DecisionTreeNode()
                                        .addOutcome(new DecisionTreeOutcome("Eligible", "true"))
                                )

                                .addBranch("Income < 3000", new DecisionTreeNode()
                                        .addOutcome(new DecisionTreeOutcome("Eligible", "false"))
                                )
                        )
                );

        DecisionTreeResult result = tree.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }
}
