package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class ModelEntityInheritance {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("human_resources");

        schema.addEntity(new EntityDef().setName("user")
                .addField(new NumericFieldDef("id", FieldType.INT))
                .addField(new TextFieldDef("email", FieldType.STRING))
                .addField(new TemporalFieldDef("created", FieldType.DATETIME)));

        schema.addEntity(new EntityDef().setName("employee")
                .setSuperEntityName("user")
                .addField(new NumericFieldDef("id", FieldType.LONG).setPosition(3))
                .addField(new TextFieldDef("email", FieldType.STRING).setMaximumLength(4096))
                .addField(new NumericFieldDef("salary", FieldType.BIG_DECIMAL).setMaximum(10_000_000))
                .addField(new TextFieldDef("department", FieldType.STRING)));

        schema.addEntity(new EntityDef().setName("manager")
                .setSuperEntityName("employee")
                .addField(new NumericFieldDef("id", FieldType.LONG).setPosition(0))
                .addField(new NumericFieldDef("salary", FieldType.BIG_DECIMAL).setMaximum(99_999_999))
                .addField(new NumericFieldDef("budget", FieldType.BIG_DECIMAL).setMaximum(5_000_000)));


        System.out.println("user ---------------------------------------------------------");
        System.out.println(schema.getEntity("user").getInheritedFields());
        System.out.println("employee ---------------------------------------------------------");
        System.out.println(schema.getEntity("employee").getInheritedFields());
        System.out.println("manager ---------------------------------------------------------");
        System.out.println(schema.getEntity("manager").getInheritedFields());
    }

}
