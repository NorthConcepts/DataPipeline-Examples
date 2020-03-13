package com.northconcepts.datapipeline.foundations.examples.decisiontree;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.decisiontree.*;
import com.northconcepts.datapipeline.foundations.expression.CalculatedField;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;

public class AddADecisionTreeToAPipeline {

    public static void main(String[] args) {

        DecisionTree tree = new DecisionTree()
                .addField(new CalculatedField("Variant Price", "toBigDecimal(${Variant Price})"))

                .setRootNode(new DecisionTreeNode()

                        .addBranch("Variant Price == null || variantPrice < 20", new DecisionTreeNode()
                            .addOutcome(new DecisionTreeOutcome("Shipping", "0.00"))
                            .addOutcome(new DecisionTreeOutcome("Total", "${variantPrice} + Shipping"))
                        )

                        .addBranch("Variant Price < 50", new DecisionTreeNode()
                                .addOutcome(new DecisionTreeOutcome("Shipping", "5.00"))
                                .addOutcome(new DecisionTreeOutcome("Total", "${variantPrice} + Shipping"))

                        )

                        .addBranch("Variant Price < 100", new DecisionTreeNode()
                                .addOutcome(new DecisionTreeOutcome("Shipping", "7.00"))
                                .addOutcome(new DecisionTreeOutcome("Total", "${variantPrice} + Shipping"))

                        )

                        .addBranch("Variant Price >= 100", new DecisionTreeNode()
                                .addOutcome(new DecisionTreeOutcome("Shipping", "${variantPrice} * 0.10"))
                                .addOutcome(new DecisionTreeOutcome("Total", "${variantPrice} + Shipping"))
                        )
                );

        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);

        reader = new DecisionTreeReader(reader, tree);

        reader = new TransformingReader(reader).add(new SelectFields("Title", "Handle", "Variant Price", "Shipping", "Total"));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
