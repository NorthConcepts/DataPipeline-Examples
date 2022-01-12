/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.common;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;

public class SlowProxyReader extends ProxyReader {

    private final long delayBeforeRead;
    private final long delayAfterRead;

    public SlowProxyReader(DataReader dataReader, long delayBeforeRead, long delayAfterRead) {
        super(dataReader);
        this.delayBeforeRead = delayBeforeRead;
        this.delayAfterRead = delayAfterRead;
    }
    
    public long getDelayBeforeRead() {
        return delayBeforeRead;
    }
    
    public long getDelayAfterRead() {
        return delayAfterRead;
    }
    
    @Override
    protected Record readImpl() throws Throwable {
        Thread.sleep(delayBeforeRead);
        Record record = super.readImpl();
        Thread.sleep(delayAfterRead);
        return record;
    }

}
