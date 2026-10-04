package com.northconcepts.datapipeline.foundations.examples.tree;

import java.io.File;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.pipeline.tree.Tree;
import com.northconcepts.datapipeline.foundations.pipeline.tree.detect.TreeDetectionStrategy;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;

public class UseARegexRuleToDetectRecords {

    public static void main(String[] args) {
        File inputFile = new File("example/data/input/tree/regex_named_rows.json");

        TreeDetectionStrategy strategy = TreeDetectionStrategy.standard(true)
                .addRegexNodeNameRule("row\\d+");

        Tree tree = Tree.loadJson(inputFile, strategy);

        JsonReader reader = new JsonReader(inputFile);
        tree.getAllFields().forEach(node -> reader.addField(node.getFieldName(), node.getXpathExpression()));
        tree.getAllRecordBreaks().forEach(node -> reader.addRecordBreak(node.getXpathExpression()));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }

}
