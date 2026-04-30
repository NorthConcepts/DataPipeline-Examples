package com.northconcepts.datapipeline.examples.jsonata;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.ValueNode;

import java.io.FileReader;

import static com.northconcepts.datapipeline.jsonata.Jsonata.jsonata;


public class QueryJsonUsingJSONata {

    public static void main(String[] args) throws Throwable {
        try (FileReader fileReader = new FileReader("data/input/nested-data.json")) {
            ValueNode data = Record.fromJson(fileReader);

            printSection("Basic Selection");
            runQuery("Query 1 - Donut Names", "name", data);
            runQuery("Query 2 - Batter Types", "batters.batter.type", data);

            printSection("Filtering");
            runQuery("Query 3 - Donut Named Raised", "$[name='Raised']", data);
            runQuery("Query 4 - Donut with Chocolate with Sprinkles Topping", "$[topping[type='Chocolate with Sprinkles']].name", data);

            printSection("Reshaping");
            runQuery("Query 5 - Name With Batter And Topping Counts",
                    "{'name': name, 'batterCount': $count(batters.batter), 'toppingCount': $count(topping)}",
                    data);
            runQuery("Query 6 - Donut Summary",
                    "{'id': id, 'name': name, 'pricePerUnit': ppu}",
                    data);

            printSection("Metrics");
            runQuery("Query 7 - Total Donuts", "$count($)", data);
            runQuery("Query 8 - Average Price Per Unit", "$average(ppu)", data);
        }
    }

    private static void runQuery(String title, String expression, ValueNode data) {
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
