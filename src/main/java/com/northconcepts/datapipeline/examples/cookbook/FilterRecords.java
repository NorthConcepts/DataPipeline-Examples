/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FieldFilter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.filter.rule.IsJavaType;
import com.northconcepts.datapipeline.filter.rule.IsNotNull;
import com.northconcepts.datapipeline.filter.rule.PatternMatch;
import com.northconcepts.datapipeline.filter.rule.ValueMatch;
import com.northconcepts.datapipeline.job.Job;

public class FilterRecords {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        FilteringReader filteringReader = new FilteringReader(reader);
        
        filteringReader.add(new FieldFilter("Rating")
                .addRule(new IsNotNull())
                .addRule(new IsJavaType(String.class))
                .addRule(new ValueMatch<String>("B", "C")));
        
        filteringReader.add(new FieldFilter("Account")
                .addRule(new IsNotNull())
                .addRule(new IsJavaType(String.class))
                .addRule(new PatternMatch("[0-9]*")));
        
        filteringReader.add(new FilterExpression(
                "parseDouble(CreditLimit) >= 0 && parseDouble(CreditLimit) <= 5000 and parseDouble(Balance) <= parseDouble(CreditLimit)"));

        DataWriter writer = new StreamWriter(System.out);

        Job.run(filteringReader, writer);
    }

}
