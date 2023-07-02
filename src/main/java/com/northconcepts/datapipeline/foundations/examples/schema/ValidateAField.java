package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.FieldDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;

public class ValidateAField {

    public static void main(String[] args) {
        FieldDef fieldDef = new NumericFieldDef("age", FieldType.INT)
                .setRequired(true)
                .setMinimum(25)
                .setMaximum(75);
        
        Record record = new Record();
        Field field = record.addField("age", 76);
        
        System.out.println(fieldDef.validateField(field));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateField(record));
        System.out.println("------------------------------------");
        
    }

}
