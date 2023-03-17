package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.Record;

public class StreamRecordFields {

    public static void main(String[] args) {
        Record record = new Record();

        record.setField("stageName", "John Wayne");
        record.setField("realName", "Marion Robert Morrison");
        record.setField("gender", "male");
        record.setField("city", "Winterset");
        record.setField("balance", 156.35);

        System.out.println("================================Using forEach================================");
        record.forEach(field -> System.out.println(field.getValueAsString()));

        System.out.println("\n\n================================Using stream================================");
        record.stream()
                .map(field -> field.getValueAsString().toUpperCase())
                .forEach(System.out::println);

        System.out.println("\n\n================================Using parallelStream================================");
        record.parallelStream()
                .map(field -> field.getValueAsString().toUpperCase())
                .forEachOrdered(System.out::println);

    }
}
