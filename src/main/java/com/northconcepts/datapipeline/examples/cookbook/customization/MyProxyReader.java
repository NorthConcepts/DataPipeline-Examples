/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;

public class MyProxyReader extends ProxyReader {
    
    public MyProxyReader(DataReader nestedDataReader) {
        super(nestedDataReader);
    }
    
    protected Record interceptRecord(Record record) throws Throwable {
        // write logic to transform Record here
        // return null to exclude this record
        // use this.push(Record) and Record.clone() to add
        // new records to this stream
        return super.interceptRecord(record);
    }

}
