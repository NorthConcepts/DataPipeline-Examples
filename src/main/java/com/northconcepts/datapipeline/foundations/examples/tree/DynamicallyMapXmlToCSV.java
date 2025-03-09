package com.northconcepts.datapipeline.foundations.examples.tree;

import java.io.File;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.foundations.pipeline.tree.Tree;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.XmlReader;

public class DynamicallyMapXmlToCSV {

    public static void main(String[] args) {
        File inputFile = new File("example/data/input/call-center-agents.xml");

        XmlReader reader = new XmlReader(inputFile);

        Tree tree = Tree.loadXml(inputFile);

        // Add all fields
        tree.getAllFields()
        .forEach(treeNode -> reader.addField(treeNode.getName(), treeNode.getXpathExpression()));

        // Add all record breaks
        tree.getAllRecordBreaks()
        .forEach(treeNode -> reader.addRecordBreak(treeNode.getXpathExpression()));

        DataWriter writer = new CSVWriter(new File("example/data/output/DynamicallyMapXmlToCSV_Output.csv"));

        Job.run(reader, writer);
    }

}
