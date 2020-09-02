/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVReader;

public class ContinueAfterAnError {

    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) {
        // open a CSV file with an error in the second record (unterminated quote)
        CSVReader csvReader = new CSVReader(new File("example/data/input/bad-credit-balance-01.csv")) {
            // override CSVReader.read() to ignore exceptions
            public Record read() throws DataException {
                try {
                    return super.read();
                } catch (DataException e) {
                	log.warn(e, e);
//                    e.printStackTrace();
                    return read(); // read the next line
                }
            }
        };
        csvReader.setFieldNamesInFirstRow(true);

        csvReader.open();
        try {
            Record record;
            while ((record = csvReader.read()) != null) {
                log.info(record);
            }
        } finally {
            csvReader.close();
        }
    }

}
