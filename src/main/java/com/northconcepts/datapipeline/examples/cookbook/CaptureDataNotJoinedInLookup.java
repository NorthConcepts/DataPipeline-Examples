/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.DataReaderLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;

public class CaptureDataNotJoinedInLookup {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance.csv"))
                .setFieldNamesInFirstRow(true);

        /*
         * This lookup matches [credit-balance-01.csv].[Rating] to
         * [rating-table-01.csv].[rating_code] to return
         * [rating-table-01.csv].[rating_description]
         */
        Lookup lookup = new DataReaderLookup(
                new CSVReader(new File("example/data/input/rating-table-01.csv"))
                        .setFieldNamesInFirstRow(true),
                new FieldList("rating_code"),
                new FieldList("rating_description"));

        MemoryWriter discardWriter = new MemoryWriter();
        
        reader = new TransformingReader(reader, discardWriter)
                .add(new DiscardLookupTransformer(new FieldList("Rating"), lookup)
                        .setAllowNoResults(true));

        Job.run(reader, new StreamWriter(System.out));
        
        System.out.println("==================================================================================");
        System.out.println("Not Joined Records: " + discardWriter.getRecordList());
    }

    private static class DiscardLookupTransformer extends LookupTransformer {

        public DiscardLookupTransformer(FieldList fields, Lookup lookup) {
            super(fields, lookup);
        }
        
        @Override
        protected RecordList noResults(Record originalRecord, List<?> arguments) {
            if (isAllowNoResults()) {
               getReader().getDiscardWriter().write(originalRecord); 
            }
            return super.noResults(originalRecord, arguments);
        }
    }
    
}
