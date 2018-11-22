/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.ProxyWriter;
import com.northconcepts.datapipeline.core.Record;

public class MyProxyWriter extends ProxyWriter {
    
    public MyProxyWriter(DataWriter nestedDataWriter) {
        super(nestedDataWriter);
    }
    
    protected Record interceptRecord(Record record) throws Throwable {
        // write logic to transform Record here
        // return null to exclude this record
        // use this.push(Record) and Record.clone() to add
        // new records to this stream
        return super.interceptRecord(record);
    }

}