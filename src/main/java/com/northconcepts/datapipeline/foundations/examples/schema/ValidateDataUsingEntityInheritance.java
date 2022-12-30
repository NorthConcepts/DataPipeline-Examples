package com.northconcepts.datapipeline.foundations.examples.schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.foundations.schema.ValidationMessage;
import com.northconcepts.datapipeline.foundations.schema.ValidationResult;

public class ValidateDataUsingEntityInheritance {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef("human_resources");

        schema.addEntity(new EntityDef().setName("auditable")
                .addField(new TextFieldDef("created_by", FieldType.STRING).setRequired(true))
                .addField(new TemporalFieldDef("created_on", FieldType.DATETIME).setRequired(true))
                .addField(new TextFieldDef("modified_by", FieldType.STRING))
                .addField(new TemporalFieldDef("modified_on", FieldType.DATETIME)));

        schema.addEntity(new EntityDef().setName("user")
                .setSuperEntityName("auditable")
                .addField(new NumericFieldDef("id", FieldType.INT).setRequired(true))
                .addField(new TextFieldDef("email", FieldType.STRING))
                .addField(new TemporalFieldDef("created", FieldType.DATETIME)));

        schema.addEntity(new EntityDef().setName("employee")
                .setSuperEntityName("user")
                .addField(new NumericFieldDef("id", FieldType.LONG))
                .addField(new TextFieldDef("email", FieldType.STRING).setMaximumLength(4096).setRequired(true))
                .addField(new NumericFieldDef("salary", FieldType.BIG_DECIMAL).setMaximum(10_000_000))
                .addField(new TextFieldDef("department", FieldType.STRING)));

        Record record = new Record();
        record.addField("id", 123456L);
        record.addField("email", "john_doe@example.com");
        record.addField("salary", BigDecimal.valueOf(200000L));
        record.addField("department", "IT");
        record.addField("budget", 100_000);
        record.addField("created_by", null); // Required field
        record.addField("created_on", LocalDateTime.of(2022, 8, 19, 15, 18));
        record.addField("created", "dummy"); // wrong value type, it should be DATETIME.
        record.addField("modified_by", "test_user");

        System.out.println("==========================Input Record====================================");
        System.out.println(record);
        System.out.println("=========================================================================================");

        ValidationResult validationResult = schema.getEntity("employee").validateRecord(record);
        System.out.println(validationResult);

        System.out.println("=========================================================================================");
        for (int i = 0; i < validationResult.getErrors().size(); i++) {
            ValidationMessage validationMessage = validationResult.getErrors().get(i);
            System.out.println(i + " - " + validationMessage.getMessage());
        }
    }
}
