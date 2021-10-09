/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */

package com.northconcepts.datapipeline.foundations.examples.tree;

import java.io.File;

import com.northconcepts.datapipeline.foundations.pipeline.dataset.Tree;
import com.northconcepts.datapipeline.internal.lang.Util;

public class CreateATreeFromJson {

    public static void main(String[] args) {
        Tree tree = Tree.fromJson(new File("example/data/input/pipeline/MOCK_DATA.json"));
        System.out.println(Util.formatJson(tree.getRootNode().toJson())); // Retrieve root node
        System.out.println("===========================================================================");
        System.out.println(tree.getAllNodes()); // Retrieve all nodes
        System.out.println("===========================================================================");
        System.out.println(Util.formatJson(tree.toJson())); // Serialize Tree to JSON
        System.out.println("===========================================================================");
        System.out.println(tree.toXml()); // Serialize Tree to XML
        System.out.println("===========================================================================");
        System.out.println(tree.toRecord()); // Serialize Tree to Record
    }

}
