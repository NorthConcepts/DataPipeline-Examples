package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class AddArrayFieldsToEntities {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("E-Commerce Schema");
        
        schema.addEntity(new EntityDef("products")
                .addField(new NumericFieldDef("product_id", FieldType.LONG).setPrimaryKeyPosition(0))
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG))
                .addField(new TextFieldDef("product_name", FieldType.STRING))
                // An array with no limit on minimum or maximum elements.
                .addField(new TextFieldDef("color", FieldType.STRING).setArray(true))
                // An array of countries with minimum 2 elements.
                .addField(new TextFieldDef("countries", FieldType.STRING).setMinimumElements(2))
                // An array of cities with minimum 2 elements and maximum 100 elements.
                .addField(new TextFieldDef("cities", FieldType.STRING).setMinimumElements(2).setMaximumElements(100))
                );

        System.out.println("=========================================================");
        System.out.println(schema.toXml());
        System.out.println("=========================================================");
    }
}
