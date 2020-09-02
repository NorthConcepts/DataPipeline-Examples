/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.foundations.datamapping;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.FieldCount;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class MapDataWithRuleBasedValidation {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef()
                .addEntity(new EntityDef("customer")
                        .addValidation(new FilterExpression("age > 12 && age < 13"))
                        .addValidation(new FieldCount(4)));
        
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
