package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.EntityRelationshipDef;
import com.northconcepts.datapipeline.foundations.schema.ForeignKeyAction;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.PrimaryKeyType;
import com.northconcepts.datapipeline.foundations.schema.RelationshipCardinality;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class AddRelationshipBetweenEntities {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("E-Commerce Schema");

        schema.addEntity(new EntityDef("orders")
                .addField(new NumericFieldDef("order_id", FieldType.LONG).setPrimaryKeyPosition(0).setPrimaryKeyType(PrimaryKeyType.TABLE))
                .addField(new NumericFieldDef("customer_id", FieldType.LONG))
                .addField(new TextFieldDef("product_name", FieldType.STRING))
                .addField(new NumericFieldDef("quantity", FieldType.LONG))
                );

        schema.addEntity(new EntityDef("customers")
                .addField(new NumericFieldDef("customer_id", FieldType.LONG).setPrimaryKeyPosition(0).setPrimaryKeyType(PrimaryKeyType.AUTO))
                .addField(new TextFieldDef("customer_name", FieldType.STRING))
                .addField(new TextFieldDef("address", FieldType.STRING))
                );

        EntityRelationshipDef entityRelationship = new EntityRelationshipDef("fk_customers_orders")
                .setPrimaryEntityName("customers")
                .setForeignEntityName("orders")
                .setCardinality(RelationshipCardinality.ONE_TO_MANY)
                .setForeignKeyFieldNames("customer_id")
                .setOnUpdateAction(ForeignKeyAction.SET_DEFAULT)
                .setOnDeleteAction(ForeignKeyAction.SET_NULL);
        schema.addEntityRelationship(entityRelationship);
        
        System.out.println("=========================================================");
        System.out.println(schema.toXml());
        System.out.println("=========================================================");
    }
}
