package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.FieldCount;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class ValidateRecordsUsingFieldsAndRules {

    public static void main(String[] args) {
        EntityDef entityDef = new EntityDef()
            .addField(new TextFieldDef("name", FieldType.STRING).setRequired(false).setAllowBlank(false).setMaximumLength(100))
            .addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(25).setMaximum(75))
            .addValidation(new FieldCount(2))
            .addValidation(new FilterExpression("!contains(email, '@example.com') && year(now()) >= 2019"));
        
        
        System.out.println(entityDef.validateRecord(new Record()
                .setField("age", 75)
                .setField("email", "henry@northpole.com")));
        
        //    "valid" : true
        
        System.out.println("------------------------------------");
        
        entityDef.setAllowExtraFieldsInValidation(false);
        
        System.out.println(entityDef.validateRecord(new Record()
                .setField("age", 75)
                .setField("email", "henry@northpole.com")));
        
        //    "valid" : false
        //    "message" : "Record is missing field(s) [name] and has unexpected field(s) [email]"

        System.out.println("------------------------------------");
        
        System.out.println(entityDef.validateRecord(new Record()
                .setField("name", "")
                .setField("age", 76)
                .setField("email", "jsmith@example.com")));

        //    "valid" : false
        //    "message" : "name is blank; expected at least 1 character(s)"
        //     "message" : "age is too large; expected maximum 75, found 76"
        //     "message" : "Record has unexpected field(s) [email]"
        //     "message" : "Record failed validation rule, expected: record has 2 fields"
        //     "message" : "Record failed validation rule, expected: record satisfies expression: !contains(email, '@example.com') && year(now()) >= 2019"
        
    }

}
