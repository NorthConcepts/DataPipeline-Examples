package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.RecordFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class MapNestedRecordUsingSchema {

    public static void main(String[] args) {
        Record record = new Record();
        record.addField("name", "John Smith");
        record.addField("age", "76");
        record.addField("balance", "31.05");
        record.addField("active", "true"); // "yes" and non-zero numbers also map to true
        record.addField("bonuses", new String[]{"100", "500", "2000"});
        record.addField("balanceAge", new double[]{0, 30, 1.05});
        record.addField("lastUpdated", "2019-12-19");
        record.addField("accounts", new Record()
                .setField("name", "Retirement")
                .setField("number", "123")
                .setField("balance", "17589.00")
                , true);
        record.addField("accounts", new Record()
                .setField("name", "Cash")
                .setField("number", null)
                .setField("balance", "881.85")
                , true);

        SchemaDef schema = new SchemaDef("schema-01");

        schema.addEntity(new EntityDef("account")
                .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("number", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(20))
                .addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL).setRequired(true).setMinimum(0).setMaximum(10_000_000)));

        schema.addEntity(new EntityDef("customer")
                .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(25).setMaximum(75))
                .addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL))
                .addField(new BooleanFieldDef("active", FieldType.BOOLEAN).setAllowedValues(null))
                .addField(new NumericFieldDef("bonuses", FieldType.DOUBLE).setArray(true))
                .addField(new NumericFieldDef("balanceAge", FieldType.DOUBLE).setArray(true))
                .addField(new TemporalFieldDef("lastUpdated", FieldType.DATE).setPattern("yyyy-MM-dd"))
                .addField(new RecordFieldDef("accounts", "account").setRequired(true).setArray(true)));


        System.out.println("Original Record-----------------------------------");
        System.out.println(record);
        //        System.out.println("Mapping Result-----------------------------------");
        //        System.out.println(schema.getEntity("customer").mapRecord(record));  // map record in place
        System.out.println("Mapping Result-----------------------------------");
        System.out.println(schema.getEntity("customer").mapAndValidateRecord(record));  // map record in place
        System.out.println("Mapped Record-----------------------------------");
        System.out.println(record);
        //        System.out.println("Validation Result-----------------------------------");
        //        System.out.println(schema.getEntity("customer").validateRecord(record));
    }

}
