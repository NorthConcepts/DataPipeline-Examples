/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import java.io.FileInputStream;
import java.io.FileWriter;

import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTree;
import com.northconcepts.datapipeline.foundations.decisiontree.DecisionTreeNode;

public class SaveAndLoadDecisionTreeToXML {

    public static void main(String[] args) throws Throwable {

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

        // Save this DecisionTree to XML
        tree.toXml(new FileWriter("example/data/output/decision-tree.xml"), true);

        // Load DecisionTree from XML.
        DecisionTree decisionTree = new DecisionTree()
            .fromXml(new FileInputStream("example/data/output/decision-tree.xml"));

        System.out.println("DecisionTree loaded from XML:-\n" + decisionTree);
    }
}
