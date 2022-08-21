package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.EntityRelationshipDef;
import com.northconcepts.datapipeline.foundations.schema.ForeignKeyAction;
import com.northconcepts.datapipeline.foundations.schema.IndexDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.RelationshipCardinality;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class ModelRelatedEntitiesAndIndexes {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("world_stats");

        schema.addEntity(new EntityDef("country")
                .addField(new TextFieldDef("code", FieldType.STRING).setPrimaryKeyPosition(0))
                .addField(new TextFieldDef("english_name", FieldType.STRING))
                .addField(new TextFieldDef("local_name", FieldType.STRING))
                .addIndex(new IndexDef("idx_english_name", "local_name").setUnique(true)));

        schema.addEntity(new EntityDef("country_population")
                .addField(new TextFieldDef("country_code", FieldType.STRING).setPrimaryKeyPosition(0))
                .addField(new NumericFieldDef("year", FieldType.INT).setPrimaryKeyPosition(1))
                .addField(new NumericFieldDef("population", FieldType.BIG_DECIMAL).setMinimum(0))
                .addField(new NumericFieldDef("gdp", FieldType.BIG_DECIMAL).setScale(0))
                .addField(new NumericFieldDef("national_debt", FieldType.BIG_DECIMAL).setScale(0))
                .addField(new NumericFieldDef("inflation", FieldType.BIG_DECIMAL).setScale(2))
                .addIndex(new IndexDef("idx_year_population", "year", "population"))
                .addIndex(new IndexDef("idx_year_gdp", "year", "gdp"))
                .addIndex(new IndexDef("idx_year_national_debt", "year", "national_debt"))
                .addIndex(new IndexDef("idx_year_inflation", "year", "inflation")));

        schema.addEntityRelationship(new EntityRelationshipDef("fk_country_country_population")
                .setPrimaryEntityName("country")
                .setForeignEntityName("country_population")
                .setCardinality(RelationshipCardinality.ONE_TO_MANY)
                .setForeignKeyFieldNames("country_code")
                .setOnUpdateAction(ForeignKeyAction.CASCADE)
                .setOnDeleteAction(ForeignKeyAction.RESTRICT));

        System.out.println("------------------");
        System.out.println(schema.toJson());
        System.out.println("------------------");
        System.out.println(schema.toXml());
        System.out.println("------------------");
    }

}
