/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.FieldCount;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;

public class ValidateRecordsUsingRules {

    public static void main(String[] args) {
        EntityDef entityDef = new EntityDef()
                .addValidation(new FieldCount(2))
                .addValidation(new FilterExpression("!contains(email, '@example.com') && year(now()) >= 2019"));
        
        
        System.out.println(entityDef.validateRecord(new Record()
                .setField("age", 76)
                .setField("email", "henry@northpole.com")));

        System.out.println("------------------------------------");
        
        System.out.println(entityDef.validateRecord(new Record()
                .setField("name", "")
                .setField("age", 76)
                .setField("email", "jsmith@example2.com")));
        
    }

}
