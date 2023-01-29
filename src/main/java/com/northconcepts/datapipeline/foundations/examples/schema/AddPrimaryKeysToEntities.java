package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class AddPrimaryKeysToEntities {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("E-Commerce Schema");
        
        schema.addEntity(new EntityDef("suppliers")
                // Primary Key
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG).setPrimaryKeyPosition(0))
                .addField(new TextFieldDef("supplier_name", FieldType.STRING))
                .addField(new TextFieldDef("city", FieldType.STRING))
                .addField(new TextFieldDef("country", FieldType.STRING))
                .addField(new TextFieldDef("contact_details", FieldType.STRING))
                );

        schema.addEntity(new EntityDef("products")
                // Composite key of product_id & supplier_id
                .addField(new NumericFieldDef("product_id", FieldType.LONG).setPrimaryKeyPosition(0))
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG).setPrimaryKeyPosition(1))
                .addField(new TextFieldDef("product_name", FieldType.STRING))
                .addField(new TextFieldDef("color", FieldType.STRING))
                );

        System.out.println("=========================================================");
        System.out.println(schema.toXml());
        System.out.println("=========================================================");
    }
}
