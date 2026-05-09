package com.northconcepts.datapipeline.examples.jsonata;

import com.northconcepts.datapipeline.avro.AvroReader;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.ValueNode;

import java.io.File;

import static com.northconcepts.datapipeline.jsonata.Jsonata.jsonata;

public class QueryAvroUsingJSONata {

    public static void main(String[] args) throws Throwable {
        ValueNode<?> data = readAvroAsValueNode("data/input/nested-data.avro");

        printSection("Basic Selection");
        runQuery("Query 1 - Donut Names", "records.name", data);
        runQuery("Query 2 - Batter Types",
                "records.batterTypes[]",
                data);

        printSection("Filtering");
        runQuery("Query 3 - Donut Named Raised",
                "records[name='Raised']",
                data);
        runQuery("Query 4 - Donut with Chocolate with Sprinkles Topping",
                "records[$count(toppingTypes[$='Chocolate with Sprinkles']) > 0].name",
                data);

        printSection("Reshaping");
        runQuery("Query 5 - Name With Batter And Topping Counts",
                "records.{'name': name, 'batterCount': $count(batterIds), 'toppingCount': $count(toppingIds)}",
                data);
        runQuery("Query 6 - Donut Summary",
                "records.{'id': id, 'name': name, 'pricePerUnit': ppu}",
                data);

        printSection("Metrics");
        runQuery("Query 7 - Total Donuts", "$count(records)", data);
        runQuery("Query 8 - Average Price Per Unit", "$average(records.ppu.$number())", data);
    }

    private static ValueNode<?> readAvroAsValueNode(String avroFilePath) throws Throwable {
        RecordList rows = new RecordList(new AvroReader(new File(avroFilePath)));
        return rows.toRecord();
    }

    private static void runQuery(String title, String expression, ValueNode<?> data) {
        System.out.println(title);
        System.out.println("Expression: " + expression);
        System.out.println("Result: ");
        System.out.println(jsonata(expression).evaluate(data));
    }

    private static void printSection(String title) {
        System.out.println("==============================================================");
        System.out.println(title);
    }
}

