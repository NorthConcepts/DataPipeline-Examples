/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;

public class MyDataWriter extends DataWriter {
    
    public void open() throws DataException {
        super.open();
        // open datasource here
    }

    public void close() throws DataException {
        // close datasource here
        super.close();
    }
    
    protected void writeImpl(Record record) throws Throwable {
        // write record to datasink here
    }
    
}
