/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.schema;

import java.math.BigDecimal;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.RecordFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class ValidateNestedRecordFields {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("schema-01");

        schema.addEntity(new EntityDef("address")
                .addField(new TextFieldDef("street", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("city", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("state_or_province", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("postal_code", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("country", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100)));

        schema.addEntity(new EntityDef("account")
                .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("number", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(20))
                .addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL).setRequired(true).setMinimum(0).setMaximum(10_000_000))
                .addField(new RecordFieldDef("address", "address").setRequired(false)));

        schema.addEntity(new EntityDef("customer")
                .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(0).setMaximum(150))
                .addField(new RecordFieldDef("account", "account").setArray(true).setRequired(true))
                .addField(new RecordFieldDef("address", "address").setRequired(true)));


        Record record = new Record();
        record.addField("name", "");
        record.addField("age", 76);
        record.addField("address", new Record()
                .setField("street", "One Microsoft Way")
                .setField("city", "Redmond")
                .setField("state_or_province", "Washington")
                .setField("postal_code", "98052-6399")
                .setField("country", "USA")
                );
        record.addField("account", new Record()
                .setField("name", "Retirement")
                .setField("number", "123")
                .setField("balance", new BigDecimal("17589.00")),
                true
                );
        record.addField("account", new Record()
                .setField("name", "Cash")
                .setField("number", null)
                .setField("balance", new BigDecimal("881.85")),
                true
                );



        System.out.println("================================");
        System.out.println(record);
        //        System.out.println("--------------------------------------------");
        //        System.out.println(record.toJson());
        //        System.out.println("--------------------------------------------");
        //        System.out.println(record.toXml());
        //        System.out.println("================================");
        //        System.out.println(Util.formatJson(schema.toJson()));
        //        System.out.println("--------------------------------------------");
        //        System.out.println(schema.toXml());
        System.out.println("================================");
        System.out.println(schema.getEntity("customer").validateRecord(record));
        System.out.println("================================");
    }

}
