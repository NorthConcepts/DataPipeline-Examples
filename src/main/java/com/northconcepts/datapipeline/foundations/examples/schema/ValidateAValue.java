package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.FieldDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;

public class ValidateAValue {

    public static void main(String[] args) {
        FieldDef fieldDef = new NumericFieldDef("age", FieldType.INT)
                .setRequired(true)
                .setMinimum(25)
                .setMaximum(75);
        
        System.out.println(fieldDef.validateValue(25));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(24));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(null));
        System.out.println("------------------------------------");
        
    }

}
