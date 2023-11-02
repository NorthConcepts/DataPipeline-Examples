package com.northconcepts.datapipeline.examples.csv;

import com.northconcepts.datapipeline.csv.CommaSeparatedValues;

public class ReadAndWriteCsvAsString {

    private static final String[] CONTINENTS = {"Africa", "Antarctica", "Asia", "Australia", "Europe", "North America", "South America"};

    public static void main(String[] args) {

        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.fromArray(CONTINENTS);
        System.out.println(commaSeparatedValues.toCsvString());

        commaSeparatedValues.remove(1);
        System.out.println(commaSeparatedValues.toCsvString());
    }
}
