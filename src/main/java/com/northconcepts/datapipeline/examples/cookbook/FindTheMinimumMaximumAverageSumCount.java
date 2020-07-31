/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class FindTheMinimumMaximumAverageSumCount {

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        // CSV fields are strings, convert them to double
        reader = new TransformingReader(reader)
            .add(new BasicFieldTransformer("Balance", "CreditLimit").stringToDouble());
        
        GroupByReader groupByReader = new GroupByReader(reader, "Rating")
            .count("Account")
            .max("Balance", "MaxBalance")
            .min("Balance", "MinBalance")
            .avg("Balance", "AvgBalance")
            .sum("Balance", "SumBalance")
            .avg("CreditLimit", "AvgCreditLimit");
        
        Job.run(groupByReader, new StreamWriter(System.out));
        
/* input csv data
Account,LastName,FirstName,Balance,CreditLimit,AccountCreated,Rating
101,Reeves,Keanu,9315.45,10000.00,1/17/1998,A
312,Butler,Gerard,90.00,1000.00,8/6/2003,B
868,Hewitt,Jennifer Love,0,17000.00,5/25/1985,B
761,Pinkett-Smith,Jada,49654.87,100000.00,12/5/2006,A
317,Murray,Bill,789.65,5000.00,2/5/2007,C
*/
/* output 
should display 3 records as the csv data are grouped according to "Rating" (A,B,C).
1st record, group "A" displays Account: 2 via count(), MaxBalance equals Jada's record,
MinBalance equals Keanu's record, AvgBalance: the average of the 2 Balances, SumBalance: sum of
the 2 Balances, AvgCreditLimit: average of Keanu and Jada's CreditLimit
*/
    }
}
