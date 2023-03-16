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
        record.setField("balance", 156.35);

        ArrayValue arrayValue = new ArrayValue();
        record.forEach(field -> arrayValue.addValue(field));

        System.out.println("================================Using forEach================================");
        arrayValue.forEach(field -> System.out.println(field.getValueAsString()));

        System.out.println("\n\n================================Using stream================================");
        arrayValue.stream()
                .filter(field -> !field.isArray())
                .map(field -> field.getValueAsString().toUpperCase())
                .forEach(fieldValue -> System.out.println(fieldValue));

        System.out.println("\n\n================================Using parallelStream================================");
        arrayValue.parallelStream()
                .filter(field -> !field.isArray())
                .map(field -> field.getValueAsString().toUpperCase())
                .forEachOrdered(fieldValue -> System.out.println(fieldValue));
    }
}
