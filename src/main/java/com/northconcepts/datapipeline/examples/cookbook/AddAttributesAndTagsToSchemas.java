package com.northconcepts.datapipeline.examples.cookbook;

import java.time.LocalDateTime;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class AddAttributesAndTagsToSchemas {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("ecommerce");

        EntityDef entity = new EntityDef("user_information")
                .addField(new NumericFieldDef("USER_ID", FieldType.INT))
                .addField(new TextFieldDef("USER_NAME", FieldType.STRING))
                .addField(new TextFieldDef("PASSWORD", FieldType.STRING))
                .addField(new TextFieldDef("FIRST_NAME", FieldType.STRING))
                .addField(new TextFieldDef("LAST_NAME", FieldType.STRING))
                .addField(new TextFieldDef("EMAIL", FieldType.STRING))
                .addField(new NumericFieldDef("USER_ROLE_ID", FieldType.INT))
                .addField(new BooleanFieldDef("ACTIVE", FieldType.BOOLEAN))
                ;

        schema.getAttributes()
            .setValue("department", "Sales")
            .setValue("modified", LocalDateTime.now());
        schema.getTags().add("Draft");

        entity.getAttributes()
            .setValue("modified", LocalDateTime.now());
        entity.getTags().add("Draft").add("User Management").add("Security");
        
        System.out.println(schema.getAttributes());
        System.out.println("------------------");
        System.out.println(schema.getAttributes().toRecord());
        System.out.println("------------------");
        System.out.println(schema.getAttributes().toXml());
        System.out.println("------------------");
        System.out.println(schema.getAttributes().toJson());
        System.out.println("================================================");
        System.out.println(entity.getTags());
        System.out.println("------------------");
        System.out.println(entity.getTags().toRecord());
        System.out.println("------------------");
        System.out.println(entity.getTags().toXml());
        System.out.println("------------------");
        System.out.println(entity.getTags().toJson());

    }

}
