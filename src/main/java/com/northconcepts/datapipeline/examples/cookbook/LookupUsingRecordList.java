package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;
import com.northconcepts.datapipeline.transform.lookup.RecordListLookup;

public class LookupUsingRecordList {

    public static void main(String[] args) {
        RecordList salesOrders = new RecordList();
        salesOrders.add(new Record().setField("order_id", 101).setField("product_id", 1).setField("quantity", 2));
        salesOrders.add(new Record().setField("order_id", 102).setField("product_id", 3).setField("quantity", 5));
        salesOrders.add(new Record().setField("order_id", 103).setField("product_id", 1).setField("quantity", 1));
        salesOrders.add(new Record().setField("order_id", 104).setField("product_id", 99).setField("quantity", 10)); // No matching product

        DataReader reader = new MemoryReader(salesOrders);

        RecordList productData = new RecordList();
        productData.add(new Record().setField("id", 1).setField("name", "Laptop").setField("price", 1200.00));
        productData.add(new Record().setField("id", 2).setField("name", "Mouse").setField("price", 25.50));
        productData.add(new Record().setField("id", 3).setField("name", "Keyboard").setField("price", 75.00));

        FieldList keyFieldsInLookup = new FieldList("id");
        FieldList valueFieldsToReturn = new FieldList("name", "price");
        RecordListLookup productLookup = new RecordListLookup(productData, keyFieldsInLookup, valueFieldsToReturn);

        FieldList keyFieldsInStream = new FieldList("product_id");
        LookupTransformer lookupTransformer = new LookupTransformer(keyFieldsInStream, productLookup);
        lookupTransformer.setAllowNoResults(true); // Prevent job failure on no match.

        DataReader transformingReader = new TransformingReader(reader).add(lookupTransformer);

        DataWriter writer = new StreamWriter(System.out);

        Job.run(transformingReader, writer);
    }
}