package com.northconcepts.datapipeline.foundations.examples.tree;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.LongAdder;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.pipeline.tree.Tree;
import com.northconcepts.datapipeline.foundations.pipeline.tree.TreeNode;

public class ShowJsonTreeNodeStatistics {

    public static void main(String[] args) {
        Tree tree = Tree.loadJson(new File("example/data/input/pipeline/MOCK_DATA.json"));
        List<TreeNode> nodes = tree.getAllNodes();
        nodes.forEach(n -> printTreeNode(n, n.getDepth()));
    }

    private static void printTreeNode(TreeNode treeNode, int depth) {
        System.out.println("==================================================");
        String spaces = printSpaceForChild(depth);
        System.out.println(spaces + "Name:" + treeNode.getName());
        System.out.println(spaces + "Value Count: " + treeNode.getValueCount());
        System.out.println(spaces + "Null Count: " + treeNode.getNullCount());
        System.out.println(spaces + "Blank Count: " + treeNode.getBlankCount());
        System.out.println(spaces + "Unique Value Count " + treeNode.getUniqueValueCount());
        System.out.println(spaces + "Is Numeric Column: " + treeNode.isNumeric());
        System.out.println(spaces + "Is Temporal Column: " + treeNode.isTemporal());
        System.out.println(spaces + "Is Boolean Column: " + treeNode.isBoolean());
        System.out.println(spaces + "Minimum Length: " + treeNode.getMinimumLength());
        System.out.println(spaces + "Maximum Length: " + treeNode.getMaximumLength());
        System.out.println(spaces + "Sample Value: " + treeNode.getSampleValue());

        for (Entry<FieldType, LongAdder> entry : treeNode.getFieldTypes().entrySet()) {
            System.out.println(spaces + "Field Type: " + entry.getKey() + " Pointer: " + entry.getValue().longValue());
        }

        System.out.println(spaces + "Depth: " + treeNode.getDepth());
        System.out.println(spaces + "Is Attribute: " + treeNode.isAttribute());
        System.out.println(spaces + "Instance Count: " + treeNode.getInstanceCount());
        System.out.println(spaces + "Minimum Child Instance Count: " + treeNode.getMinimumChildInstanceCount());
        System.out.println(spaces + "Maximum Child Instance Count: " + treeNode.getMaximumChildInstanceCount());
        System.out.println(spaces + "Is Record Break: " + treeNode.isRecordBreak());
        System.out.println(spaces + "Is Field: " + treeNode.isField());
        System.out.println(spaces + "Is Cascade Field Value: " + treeNode.isCascadeFieldValue());
        System.out.println(spaces + "Field Name: " + treeNode.getFieldName());
        System.out.println(spaces + "XPath Expression: " + treeNode.getXpathExpression());
        System.out.println(spaces + "Descendant Value Count: " + treeNode.getDescendantValueCount());
        System.out.println(spaces + "Children Value Count: " + treeNode.getChildrenValueCount());
        System.out.println(spaces + "Children TreeNode Count: " + treeNode.getChildren().size());

    }

    private static String printSpaceForChild(int depth) {
        StringBuilder sb  = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

}
