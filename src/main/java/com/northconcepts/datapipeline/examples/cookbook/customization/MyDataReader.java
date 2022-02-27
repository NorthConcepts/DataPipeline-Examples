/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;

public class MyDataReader extends DataReader {
    
    public void open() throws DataException {
        super.open();
        // open datasource here
    }

    public void close() throws DataException {
        // close datasource here
        super.close();
    }
    
    protected Record readImpl() throws Throwable {
        // write logic to return new instances of Record here
        // return null for end-of-file
        return null;
    }
    
}
