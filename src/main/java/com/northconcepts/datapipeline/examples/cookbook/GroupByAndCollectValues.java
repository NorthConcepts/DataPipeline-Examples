package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class GroupByAndCollectValues {

    public static void main(String[] args) {
        RecordList recordList = new RecordList();
        recordList.add(createSale("Electronics", "Laptop", 1200));
        recordList.add(createSale("Books", "Novel", 15));
        recordList.add(createSale("Electronics", "Mouse", 25));
        recordList.add(createSale("Clothing", "Shirt", 30));
        recordList.add(createSale("Books", "Textbook", 90));
        recordList.add(createSale("Electronics", "Mouse", 20)); //duplicate product


        DataReader reader1 = new MemoryReader(recordList);
        GroupByReader groupByReader1 = new GroupByReader(reader1, "Category");

        groupByReader1
                // Sum the "Price" field and store the result in a new "TotalSales" field.
                .sum("Price", "TotalSales")
                // Collect values from the "Product" field into a new array field named "Products".
                .collect("Product", "Products", false, false, true);

        DataWriter writer1 = new StreamWriter(System.out);

        System.out.println("--- Grouped Results (collecting all products) ---");
        Job.run(groupByReader1, writer1);
        System.out.println("--------------------------------------------------\n");
        
        
        DataReader reader2 = new MemoryReader(recordList);
        GroupByReader groupByReader2 = new GroupByReader(reader2, "Category");
        groupByReader2
                .sum("Price", "TotalSales")
                //set the 'distinctValues' parameter to true.
                .collect("Product", "DistinctProducts", true, false, true);
        
        DataWriter writer2 = new StreamWriter(System.out);
        
        System.out.println("-- Grouped Results (collecting distinct products) --");
        Job.run(groupByReader2, writer2);
        System.out.println("----------------------------------------------------");

    }

    private static Record createSale(String category, String product, double price) {
        Record record = new Record();
        record.setField("Category", category);
        record.setField("Product", product);
        record.setField("Price", price);
        return record;
    }

}