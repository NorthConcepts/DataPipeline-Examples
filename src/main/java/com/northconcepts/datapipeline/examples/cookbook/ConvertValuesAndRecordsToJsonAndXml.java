package com.northconcepts.datapipeline.examples.cookbook;

import java.math.BigDecimal;
import java.util.Arrays;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.ValueNode;

public class ConvertValuesAndRecordsToJsonAndXml {

    public static void main(String[] args) {
        Record record = new Record()
                .setField("Account", 101)
                .setField("Name", "Keanu Reeves");

        System.out.println(record.toJson());
        System.out.println(record.toXml());

        ValueNode<?> tags = ValueNode.from(Arrays.asList("vip", "early-adopter"));
        System.out.println(tags.toJson());
        System.out.println(tags.toXml());

        ValueNode<?> balance = ValueNode.from(new BigDecimal("9315.45"));
        System.out.println(balance.toJson());
        System.out.println(balance.asSingleValue().getValueAsString());
    }

}
