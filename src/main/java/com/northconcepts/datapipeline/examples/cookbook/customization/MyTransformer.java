/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.transform.Transformer;

public class MyTransformer extends Transformer {

    public MyTransformer() {
    }

    public boolean transform(Record record) throws Throwable {
        Field creditLimit = record.getField("CreditLimit");
        // increase everyone's limit by 10%
        double newValue = Double.parseDouble(creditLimit.getValueAsString()) * 1.10;
        creditLimit.setValue(newValue);
        return true;
    }

}
