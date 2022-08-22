/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.schema;

import java.util.Arrays;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.schema.FieldDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;

public class ValidateAnArray {

    public static void main(String[] args) {
        FieldDef fieldDef = new NumericFieldDef("age", FieldType.INT)
                .setRequired(true)
                .setMinimum(25)
                .setMaximum(75)
                .setArray(true)
                ;

        System.out.println(fieldDef.validateValue(new int[]{25, 90, 40}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(25));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(new int[]{25}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(new int[]{250, 30, 50, 500}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(Arrays.asList(250, 30, 50, 500)));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(Arrays.asList(25)));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(Arrays.asList(25, 30, 50)));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(new int[]{24}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(new Integer[]{24}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(new Integer[]{25}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(new Integer[]{null}));
        System.out.println("------------------------------------");
        System.out.println(fieldDef.validateValue(null));
        System.out.println("------------------------------------");

    }

}
