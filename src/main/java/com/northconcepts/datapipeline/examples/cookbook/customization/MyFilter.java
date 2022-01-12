/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.Filter;

public class MyFilter extends Filter {
    
    private final double minimumBalance;
    
    public MyFilter(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public boolean allow(Record record) {
        return record.getField("Balance").getValueAsDouble() >= minimumBalance;
    }

    public String toString() {
        return "Balance >=  " + minimumBalance;
    }

}
