package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.RecordFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.foundations.schema.ValidationMessage;
import com.northconcepts.datapipeline.foundations.schema.ValidationResult;

public class ValidateDataUsingNestedEntities {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef();

        schema.addEntity(new EntityDef("products")
                .addField(new NumericFieldDef("product_id", FieldType.LONG))
                .addField(new TextFieldDef("product_name", FieldType.STRING)));

        schema.addEntity(new EntityDef("suppliers")
                .setAllowExtraFieldsInValidation(false)
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG).setPrimaryKeyPosition(0))
                .addField(new TextFieldDef("supplier_name", FieldType.STRING).setPrimaryKeyPosition(1))
                .addField(new TextFieldDef("country", FieldType.STRING))
                .addField(new RecordFieldDef("products_record_field", "products").setArray(true).setMaximumElements(5)));

        Record magazineProduct = new Record()
                .setField("product_id", 1) // This field should be LONG instead of INT
                .setField("product_name", "Magazine");

        Record newsPaperProduct = new Record()
                .setField("product_id", 1) // This field should be LONG instead of INT
                .setField("product_name", "News Paper");

        Record suppierRecord = new Record()
                .setField("supplier_id", 1) // This field should be LONG instead of INT
                .setField("supplier_name", "A1")
                .setField("country", "USA")
                .setField("city", "New York") // Unexpected field
                .setField("products_record_field", new Record[] { magazineProduct, newsPaperProduct });

        System.out.println("==========================Input Record====================================");
        System.out.println(suppierRecord);
        System.out.println("=========================================================================================");

        ValidationResult validationResult = schema.getEntity("suppliers").validateRecord(suppierRecord);
        System.out.println(validationResult);

        System.out.println("=========================================================================================");
        for (int i = 0; i < validationResult.getErrors().size(); i++) {
            ValidationMessage validationMessage = validationResult.getErrors().get(i);
            System.out.println(i + " - " + validationMessage.getMessage());
        }

    }
}
