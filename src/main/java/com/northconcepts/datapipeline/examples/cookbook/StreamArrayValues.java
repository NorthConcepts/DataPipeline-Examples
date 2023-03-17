package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.Record;

public class StreamArrayValues {

    public static void main(String[] args) {
        Record record = new Record();

        record.setField("stageName", "John Wayne");
        record.setField("realName", "Marion Robert Morrison");
        record.setField("gender", "male");
        record.setField("city", "Winterset");
        record.setField("balance", new double[]{156.35, 123.45, 789.01, 234.56, 345.67, 456.78, 567.89, 678.90, 789.01, 890.12});

        ArrayValue arrayValue = record.getFieldValueAsArray("balance", null);

        if(arrayValue == null) {
            System.out.println("ArrayValue is null");
            return;
        }

        System.out.println("================================Using forEach================================");
        arrayValue.forEach(field -> System.out.println(field.getValueAsString()));

        System.out.println("\n\n================================Using stream================================");
        arrayValue.stream()
                .map(balance -> Double.valueOf(balance.getValueAsString()))
                .filter(balance -> balance > 210.2)
                .forEach(System.out::println);

        System.out.println("\n\n================================Using parallelStream================================");
        arrayValue.parallelStream()
                .map(balance -> Double.valueOf(balance.getValueAsString()))
                .filter(balance -> balance > 210.2)
                .forEach(System.out::println);
    }
}
