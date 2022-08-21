package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class ShowSchemaProblems {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("world_stats");

        SchemaDef addEntity = schema.addEntity(new EntityDef("country")
                .addField(new TextFieldDef("code", FieldType.STRING).setPrimaryKeyPosition(0))
                .addField(new TextFieldDef("code", FieldType.STRING).setPrimaryKeyPosition(0)));

        schema.addEntity(new EntityDef("country_population")
                .addField(new TextFieldDef("country_code", FieldType.STRING).setPrimaryKeyPosition(0))
                .addField(new NumericFieldDef("year", FieldType.INT).setPrimaryKeyPosition(1))
                .addField(new NumericFieldDef("population", FieldType.BIG_DECIMAL)));

        schema.addEntity(new EntityDef("country_population"));

        System.out.println("----------------Direct Entity Problems------------------");
        System.out.println(schema.getEntity("country").getSchemaProblems());
        System.out.println("----------------Direct Schema Problems------------------");
        System.out.println(schema.getSchemaProblems());
        System.out.println("----------------All Schema Problems------------------");
        System.out.println(schema.getSchemaProblems(true));
        System.out.println("------------------");
    }

}
