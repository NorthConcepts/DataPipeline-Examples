/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

public class MyLookup extends Lookup {
	
    public RecordList get(Object ... keys) {
        if (keys == null || keys.length != 1) {
            return null;
        }
        
        Record record = new Record();
        Field ratingClass = record.addField().setName("ratingClass");
        
        char rating = keys[0].toString().charAt(0);
        
        switch (rating) {
        case 'A': ratingClass.setValue("Class A"); break;           
        case 'B': ratingClass.setValue("Class B"); break;           
        case 'C': ratingClass.setValue("Class C"); break;           
        default: return null;           
        }
        
        RecordList list = new RecordList();
        list.add(record);
        return list;
    }

}
