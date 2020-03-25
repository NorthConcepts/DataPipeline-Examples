package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class MapDataWithSchemaBasedValidation {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef()
                .addEntity(new EntityDef("customer")
                        .addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setMinimumLength(50).setMaximumLength(100))
                        .addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(25).setMaximum(75)));
        
        EntityDef customerEntity = schema.getEntity("customer"); 

        
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");
        input.setValue("age", 15);

        DataMapping mapping = new DataMapping()
                .setEntity(customerEntity)  // set optional entity definition to validate against
                .addFieldMapping("name", "source.fname + ' ' + source.lname")
                .addFieldMapping("age", "source.age");
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();

        System.out.println("target = " + target);
        
        result.logExceptions();
        result.logValidationErrors();  // validation errors are separate from exceptions
        
    }

}
