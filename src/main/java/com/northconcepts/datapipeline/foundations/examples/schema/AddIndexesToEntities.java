package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.IndexDef;
import com.northconcepts.datapipeline.foundations.schema.IndexFieldDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class AddIndexesToEntities {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("E-Commerce Schema");
        
        schema.addEntity(new EntityDef("suppliers")
                // Primary Key
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG).setPrimaryKeyPosition(0))
                .addField(new TextFieldDef("supplier_name", FieldType.STRING))
                .addField(new TextFieldDef("city", FieldType.STRING))
                .addField(new TextFieldDef("country", FieldType.STRING))
                .addField(new TextFieldDef("contact_details", FieldType.STRING))
                // A unique index for supplier_name
                .addIndex(new IndexDef().setName("idx_supplier_name")
                        // index for supplier_name with ascending order.
                        .addIndexField(new IndexFieldDef("supplier_name", true))
                        .setUnique(true))
                .addIndex(new IndexDef("idx_supplier_name_city", "supplier_name", "city"))
                );

        System.out.println("=========================================================");
        System.out.println(schema.getEntity("suppliers").getIndexes());
        System.out.println("=========================================================");
        schema.getEntity("suppliers").getIndexes().forEach(p -> System.out.println(p.toRecord()));
        System.out.println("=========================================================");
        schema.getEntity("suppliers").getIndexes().forEach(p -> System.out.println(p.toXml()));
        System.out.println("=========================================================");
    }
}
