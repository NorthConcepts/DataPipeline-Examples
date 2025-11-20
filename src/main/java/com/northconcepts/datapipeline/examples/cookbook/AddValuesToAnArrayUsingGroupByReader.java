package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class AddValuesToAnArrayUsingGroupByReader {

    public static void main(String[] args) {
        RecordList recordList = new RecordList();
        recordList.add(createSale("Electronics", "Laptop", 1200));
        recordList.add(createSale("Books", "Novel", 15));
        recordList.add(createSale("Electronics", "Mouse", 25));
        recordList.add(createSale("Clothing", "Shirt", 30));
        recordList.add(createSale("Books", "Textbook", 90));

        GroupByReader groupByReader = new GroupByReader(new MemoryReader(recordList), "Category");

        groupByReader
                // Sum the "Price" field and store the result in a new "TotalSales" field.
                .sum("Price", "TotalSales")
                // Collect values from the "Product" field into a new array field named "Products".
                .collect("Product", "Products", false, false, true);

        Job.run(groupByReader, new StreamWriter(System.out));
    }

    private static Record createSale(String category, String product, double price) {
        Record record = new Record();
        record.setField("Category", category);
        record.setField("Product", product);
        record.setField("Price", price);
        return record;
    }

}