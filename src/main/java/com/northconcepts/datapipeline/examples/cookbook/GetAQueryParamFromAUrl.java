/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.net.GetUrlQueryParam;

import java.io.File;

public class GetAQueryParamFromAUrl {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/watch-list.csv"))
                .setFieldNamesInFirstRow(true);

        TransformingReader transformingReader = new TransformingReader(reader);

        transformingReader.add(new GetUrlQueryParam("watch", "v", "param"));

        Job.run(transformingReader, new StreamWriter(System.out));
    }
}
