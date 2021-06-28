/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Messages;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FieldFilter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.filter.rule.IsJavaType;
import com.northconcepts.datapipeline.filter.rule.IsNotNull;
import com.northconcepts.datapipeline.filter.rule.PatternMatch;
import com.northconcepts.datapipeline.filter.rule.ValueMatch;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.validate.ValidatingReader;

public class ValidateIncomingData {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        ValidatingReader validatingReader = new ValidatingReader(reader)
            .setExceptionOnFailure(false)
            .setRecordStackTraceInMessage(false);
        
        validatingReader.add(new FieldFilter("Rating")
                .addRule(new IsNotNull())
                .addRule(new IsJavaType(String.class))
                .addRule(new ValueMatch<String>("A", "B", "Z")));  // should be A, B, C
        
        validatingReader.add(new FieldFilter("Account")
                .addRule(new IsNotNull())
                .addRule(new IsJavaType(String.class))
                .addRule(new PatternMatch("[0-7]*")));  // should be [0-9]*
        
        validatingReader.add(new FilterExpression(
                "parseDouble(CreditLimit) >= 0 && parseDouble(CreditLimit) <= 100000 and parseDouble(Balance) <= parseDouble(CreditLimit)"));

        DataWriter writer = new StreamWriter(System.out);

        Job.run(validatingReader, writer);
        
        log.info("messages: " + Messages.getCurrent());
    }

}
