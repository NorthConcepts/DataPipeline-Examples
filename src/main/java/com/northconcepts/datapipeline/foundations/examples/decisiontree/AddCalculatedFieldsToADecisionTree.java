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

                        .addBranch("overAgeThreshold == true", new DecisionTreeNode()
                                .addBranch("houseOwned == true", new DecisionTreeNode().addOutcome("Eligible", "true"))

                                .addBranch("houseOwned == false", new DecisionTreeNode()
                                        .addBranch("Income >= 2000", new DecisionTreeNode().addOutcome("Eligible", "true"))
                                        .addBranch("Income < 2000", new DecisionTreeNode().addOutcome("Eligible", "false"))
                                )
                        )

                        .addBranch("overAgeThreshold == false", new DecisionTreeNode()
                                .addBranch("Income >= 3000", new DecisionTreeNode().addOutcome("Eligible", "true"))
                                .addBranch("Income < 3000", new DecisionTreeNode().addOutcome("Eligible", "false"))
                        )
                );

        DecisionTreeResult result = tree.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }
}
