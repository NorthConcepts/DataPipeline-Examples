/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.blog;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.SortingReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FieldFilter;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class ConvertTableToTree {

    private static File INPUT = new File("example/data/input/financial-transactions.csv");

    public static void main(String[] args) {
        DataReader reader = new CSVReader(INPUT).setFieldNamesInFirstRow(true).setSkipEmptyRows(true);
        
        reader = new FilteringReader(reader)
                .add(new FieldFilter("Transaction Type").valueMatches("Credit", "Debit"));
        
        reader = new TransformingReader(reader)
                .add(new SelectFields("Account", "Transaction Type", "Date", "Amount"));
        
        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("Date").stringToDate("yyyy-MM-dd"))
                .add(new BasicFieldTransformer("Amount").stringToBigDecimal());


        reader = new SortingReader(reader).asc("Account").asc("Transaction Type").asc("Date").asc("Amount");
        
        reader = new GroupByReader(reader, "Account").add(new GroupTree("data"));
        
//        DataWriter writer = StreamWriter.newJsonSystemOutWriter();
        DataWriter writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
    }

}
