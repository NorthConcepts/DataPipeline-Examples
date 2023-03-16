package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;

public class StreamFieldNames {

    public static void main(String[] args) {
        Record record = new Record();

        record.setField("stageName", "John Wayne");
        record.setField("realName", "Marion Robert Morrison");
        record.setField("gender", "male");
        record.setField("city", "Winterset");
        record.setField("balance", 156.35);

        FieldList fieldList = record.getFieldNameList();

        System.out.println("================================Using forEach================================");
        fieldList.forEach(fieldName -> System.out.println(fieldName));

        System.out.println("\n\n================================Using stream================================");
        fieldList.stream()
                .map(fieldName -> fieldName.toUpperCase())
                .forEach(fieldName -> System.out.println(fieldName));

        System.out.println("\n\n================================Using parallelStream================================");
        fieldList.parallelStream()
                .map(fieldName -> fieldName.toUpperCase())
                .forEach(fieldName -> System.out.println(fieldName));

    }
}
