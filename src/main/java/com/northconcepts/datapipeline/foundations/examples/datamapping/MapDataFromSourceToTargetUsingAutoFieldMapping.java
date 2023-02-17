/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class MapDataFromSourceToTargetUsingAutoFieldMapping {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");
        input.setValue("age", "25");
        input.setValue("city", "NY");
        input.setValue("country", "USA");

        DataMapping mapping = new DataMapping()
                .setAutoMapping(true) // enable auto fields mapping
                .addExcludedFields("city", "country") // exclude fields from auto mapping.
                .addFieldMapping("full_name", "source.fname + ' ' + source.lname"); // custom field mapping. (optional)
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();

        System.out.println("target = " + target);
        
        result.logExceptions();
    }

}
