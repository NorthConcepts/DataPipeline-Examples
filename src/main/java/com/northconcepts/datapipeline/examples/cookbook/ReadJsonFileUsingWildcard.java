package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;

import java.io.File;

public class ReadJsonFileUsingWildcard {

    public static void main(String[] args) {
        DataReader reader = new JsonReader(new File("example/data/input/products.json"))
                .addRecordBreak("//array/object")
                .addField("Id", "//array/object/id")
                .addField("Name", "//array/object/name")
                .addField("Price", "//array/object/`price (*)`") // A backtick is used to escape special symbols, and * will match all price nodes that have any characters inside the parentheses
                .addField("Currency", "//array/object/currency**") // An * is added to escape * that is in the original field
                ;
        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
