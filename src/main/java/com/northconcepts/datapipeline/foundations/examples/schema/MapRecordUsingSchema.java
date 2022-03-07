/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class MapRecordUsingSchema {

    public static void main(String[] args) {
        Record record = new Record();
        record.addField("name", "John Smith");
        record.addField("age", "76");
        record.addField("balance", "31.05");
        record.addField("active", "true"); // "yes" and non-zero numbers also map to true
        record.addField("bonuses", new String[]{"100", "500", "2000"});
        record.addField("balanceAge", new double[]{0, 30, 1.05});
        record.addField("lastUpdated", "2019-12-19");

        EntityDef entityDef = new EntityDef();
        entityDef.addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100));
        entityDef.addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(25).setMaximum(75));
        entityDef.addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL));
        entityDef.addField(new BooleanFieldDef("active", FieldType.BOOLEAN).setAllowedValues(null));
        entityDef.addField(new NumericFieldDef("bonuses", FieldType.DOUBLE).setArray(true));
        entityDef.addField(new NumericFieldDef("balanceAge", FieldType.DOUBLE).setArray(true));
        entityDef.addField(new TemporalFieldDef("lastUpdated", FieldType.DATE).setPattern("yyyy-MM-dd"));


        System.out.println("Original Record-----------------------------------");
        System.out.println(record);
        System.out.println("Mapping Result-----------------------------------");
        System.out.println(entityDef.mapRecord(record));  // map record in place
        System.out.println("Mapped Record-----------------------------------");
        System.out.println(record);
        System.out.println("Validation Result-----------------------------------");
        System.out.println(entityDef.validateRecord(record));
    }

}
