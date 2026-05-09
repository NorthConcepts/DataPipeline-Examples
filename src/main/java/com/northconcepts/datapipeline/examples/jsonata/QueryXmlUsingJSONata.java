package com.northconcepts.datapipeline.examples.jsonata;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.ValueNode;

import java.io.FileReader;

import static com.northconcepts.datapipeline.jsonata.Jsonata.jsonata;

public class QueryXmlUsingJSONata {

    public static void main(String[] args) throws Throwable {
        try (FileReader fileReader = new FileReader("data/input/nested-data.xml")) {
            ValueNode<?> xmlRoot = Record.fromXml(fileReader);

            printSection("Basic Selection");
            runQuery("Query 1 - Donut Names", "document.record.name", xmlRoot);

            runQuery("Query 2 - Batter Types", "document.record.batters.batter.type", xmlRoot);

            printSection("Filtering");
            runQuery("Query 3 - Donut Named Raised", "document.record[name='Raised']", xmlRoot);
            runQuery("Query 4 - Donut with Chocolate with Sprinkles Topping", "document.record[topping[type='Chocolate with Sprinkles']].name", xmlRoot);

            printSection("Reshaping");
            runQuery("Query 5 - Name With Batter And Topping Counts",

                    "document.record.{'name': name, 'batterCount': $count(batters.batter), 'toppingCount': $count(topping)}",
                    xmlRoot);
            runQuery("Query 6 - Donut Summary",
                    "document.record.{'id': id, 'name': name, 'pricePerUnit': ppu}",
                    xmlRoot);

            printSection("Metrics");
            runQuery("Query 7 - Total Donuts", "$count(document.record)", xmlRoot);
            runQuery("Query 8 - Average Price Per Unit", "$average(document.record.ppu.$number())", xmlRoot);
        }
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
