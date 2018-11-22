/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonReader;
import com.northconcepts.datapipeline.transform.SetField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class ConditionallyTransformData {

    public static void main(String[] args) {
        DataReader reader = new SimpleJsonReader(
                new File("example/data/input/simple-json-input.json"));
        
        reader = new TransformingReader(reader)
                .setCondition(new FilterExpression("balance < 0"))
                .add(new SetField("balance", 0.0));
        
        DataWriter writer = new StreamWriter(System.out);
        
        Job.run(reader, writer);
    }
/* input
[{"stageName":"John Wayne","realName":"Marion Robert Morrison","gender":"male","city":"Winterset","balance":156.35},
{"stageName":"Spiderman","realName":"Peter Parker","gender":"male","city":"New York","balance":-0.96}]
*/
// output - spiderman's balance becomes zero
}
