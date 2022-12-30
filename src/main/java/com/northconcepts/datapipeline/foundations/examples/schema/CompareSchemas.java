package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.RecordFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.foundations.schema.diff.SchemaDefDiff;

public class CompareSchemas {

    public static void main(String[] args) {
        SchemaDef oldSchema = new SchemaDef("schema-01");

        oldSchema.addEntity(new EntityDef("account")
                .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new TextFieldDef("number", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(20))
                .addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL).setRequired(true).setMinimum(0).setMaximum(10_000_000)));

        oldSchema.addEntity(new EntityDef("customer")
                .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100))
                .addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(25).setMaximum(75))
                .addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL))
                .addField(new BooleanFieldDef("active", FieldType.BOOLEAN).setAllowedValues(null))
                .addField(new NumericFieldDef("bonuses", FieldType.DOUBLE).setArray(true))
                .addField(new NumericFieldDef("balanceAge", FieldType.DOUBLE).setArray(true))
                .addField(new TemporalFieldDef("lastUpdated", FieldType.DATE).setPattern("yyyy-MM-dd"))
                .addField(new RecordFieldDef("accounts", "account").setRequired(true).setArray(true)));

        SchemaDef newSchema = oldSchema.clone();
        EntityDef customerEntity = newSchema.getEntity("customer");

        // Update property of FieldDef
        customerEntity.getField("age").setRequired(false);

        // Remove field from customer entity
        customerEntity.getFields().remove(customerEntity.indexOfField("balanceAge"));

        // Add a new field to cutomer entity
        customerEntity.addField(new TemporalFieldDef("created_on", FieldType.DATETIME).setPattern("yyyy-MM-dd HH:mm:ss"));

        // Remove account entity
        newSchema.getEntities().remove(newSchema.indexOfEntity("account"));

        // Add new entity to schema
        newSchema.addEntity(new EntityDef("bank")
                .addField(new TextFieldDef("bank_name", FieldType.STRING).setRequired(true))
                );

        SchemaDefDiff diff = SchemaDefDiff.diff(oldSchema, newSchema);
        // All of above updates to newSchema are reported as
        // 1. CHANGED - if property is just updated.
        // 2. ADDED - if a new property is added.
        // 3. REMOVED - if an existing property is removed.
        // 4. NONE - if there is no change for specific property.
        System.out.println("Schema Diff: " + diff);
    }
}
