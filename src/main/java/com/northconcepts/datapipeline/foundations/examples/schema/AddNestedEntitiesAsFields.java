package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.PrimaryKeyType;
import com.northconcepts.datapipeline.foundations.schema.RecordFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class AddNestedEntitiesAsFields {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("E-Commerce Schema");

        schema.addEntity(new EntityDef("orders")
                .addField(new NumericFieldDef("order_id", FieldType.LONG).setPrimaryKeyPosition(0).setPrimaryKeyType(PrimaryKeyType.TABLE))
                .addField(new RecordFieldDef("customer", "customers"))
                .addField(new TextFieldDef("product_name", FieldType.STRING))
                .addField(new NumericFieldDef("quantity", FieldType.LONG))
                );

        schema.addEntity(new EntityDef("customers")
                .addField(new NumericFieldDef("customer_id", FieldType.LONG).setPrimaryKeyPosition(0).setPrimaryKeyType(PrimaryKeyType.AUTO))
                .addField(new TextFieldDef("customer_name", FieldType.STRING))
                .addField(new TextFieldDef("address", FieldType.STRING))
                );

        System.out.println("=========================================================");
        System.out.println(schema.toXml());
        System.out.println("=========================================================");
    }
}
