package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.ValueNode;

import java.util.Comparator;

public class SortArrayValuesUsingCustomComparator {
    public static void main(String[] args) {
        ArrayValue employees = new ArrayValue();

        employees.addValue(new Record()
            .setField("id", 105)
            .setField("firstName", "John")
            .setField("lastName", "Smith")
            .setField("salary", 75000.50)
            .setField("active", true));

        employees.addValue(new Record()
            .setField("id", 102)
            .setField("firstName", "Alice")
            .setField("lastName", "Johnson")
            .setField("salary", 85000.75)
            .setField("active", true));

        employees.addValue(new Record()
            .setField("id", 108)
            .setField("firstName", "Bob")
            .setField("lastName", "Williams")
            .setField("salary", 65000.25)
            .setField("active", false));

        employees.addValue(new Record()
            .setField("id", 101)
            .setField("firstName", "Carol")
            .setField("lastName", "Davis")
            .setField("salary", 92000.00)
            .setField("active", true));

        System.out.println("Before sort:");
        printEmployees(employees);

        // Sort by ID (integer field) in ascending order
        System.out.println("\nSorting by ID (ascending):");
        employees.sort(Comparator.comparing(node ->
            ((Record) node).getField("id").getValueAsInteger()
        ));

        printEmployees(employees);

        // Sort by last name (string field) alphabetically
        System.out.println("\nSorting by Last Name (alphabetically):");
        employees.sort(Comparator.comparing(node ->
            ((Record) node).getField("lastName").getValueAsString()
        ));

        printEmployees(employees);

        // Sort by salary (double field) in descending order
        System.out.println("\nSorting by Salary (descending):");
        employees.sort(Comparator.comparing((ValueNode<?> node) ->
            ((Record) node).getField("salary").getValueAsDouble()
        ).reversed());

        printEmployees(employees);

        // Sort by active status (boolean field), then by last name
        System.out.println("\nSorting by Active status (active first), then by Last Name:");
        employees.sort(
            Comparator.comparing((ValueNode<?> node) ->
                ((Record) node).getField("active").getValueAsBoolean()
            ).reversed()
            .thenComparing(node ->
                ((Record) node).getField("lastName").getValueAsString()
            )
        );

        printEmployees(employees);
    }

    private static void printEmployees(ArrayValue employees) {
        employees.forEach(node -> {
            Record rec = (Record) node;
            System.out.println("  ID: " + rec.getField("id").getValueAsInteger() +
                             ", Name: " + rec.getField("firstName").getValueAsString() + " " + rec.getField("lastName").getValueAsString() +
                             ", Salary: $" + rec.getField("salary").getValueAsDouble() +
                             ", Active: " + rec.getField("active").getValueAsBoolean());
        });
    }
}
