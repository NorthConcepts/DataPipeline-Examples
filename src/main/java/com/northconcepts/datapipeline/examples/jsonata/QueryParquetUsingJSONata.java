package com.northconcepts.datapipeline.examples.jsonata;

import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.ValueNode;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;

import java.io.File;

import static com.northconcepts.datapipeline.jsonata.Jsonata.jsonata;

public class QueryParquetUsingJSONata {

    public static void main(String[] args) throws Throwable {
        ValueNode<?> data = readParquetAsValueNode("data/input/nested-data.parquet");

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

    private static ValueNode<?> readParquetAsValueNode(String parquetFilePath) throws Throwable {
        RecordList rows = new RecordList(new ParquetDataReader(new File(parquetFilePath)));
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

