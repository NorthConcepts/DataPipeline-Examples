package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class ExecuteAnActionInADecisionTree {

    public static void action1() {
        System.out.println("Action 1");
    }

    public static double action2(int age, double income) {
        System.out.println("Age: " + age + ";  Income: " + income);
        return age * income;
    }

    public static void main(String[] args) {
        Functions.add("action2", "com.northconcepts.datapipeline.foundations.examples.decisiontree.ExecuteAnActionInADecisionTree.action2");

        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("Age", 49);
        input.setValue("houseOwned", true);
        input.setValue("Income", 1000.0);

        DecisionTree tree = new DecisionTree()
                .setRootNode(new DecisionTreeNode()

                    .addBranch("Age >= 40", new DecisionTreeNode()

                        .addBranch("houseOwned == true", new DecisionTreeNode()
                            .addOutcome("Eligible", "true")
                            .addOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontree.ExecuteAnActionInADecisionTree.action1()")
                            .addOutcome("Action2", "action2(Age, Income)")
                        )

                        .addBranch("houseOwned == false", new DecisionTreeNode()

                            .addBranch("Income >= 2000", new DecisionTreeNode()
                                .addOutcome("Eligible", "true")
                                .addOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontree.ExecuteAnActionInADecisionTree.action1()")
                                .addOutcome("Action2", "action2(Age, Income)")
                            )

                            .addBranch("Income < 2000", new DecisionTreeNode()
                                .addOutcome("Eligible", "false")
                                .addOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontree.ExecuteAnActionInADecisionTree.action1()")
                                .addOutcome("Action2", "action2(Age, Income)")
                            )
                        )
                    )

                    .addBranch("Age < 40", new DecisionTreeNode()

                        .addBranch("Income >= 3000", new DecisionTreeNode()
                                .addOutcome("Eligible", "true")
                                .addOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontree.ExecuteAnActionInADecisionTree.action1()")
                                .addOutcome("Action2", "action2(Age, Income)")
                        )

                        .addBranch("Income < 3000", new DecisionTreeNode()
                                .addOutcome("Eligible", "false")
                                .addOutcome("Action1", "com.northconcepts.datapipeline.foundations.examples.decisiontree.ExecuteAnActionInADecisionTree.action1()")
                                .addOutcome("Action2", "action2(Age, Income)")
                        )
                    )
                );

        DecisionTreeResult result = tree.evaluate(input);
        Record outcome = result.getOutcome();

        System.out.println("outcome = " + outcome);
    }
}
