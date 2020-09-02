/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.foundations.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class ValidateRecordFields {

    public static void main(String[] args) {
        EntityDef entityDef = new EntityDef();
        entityDef.addField(new TextFieldDef("name", FieldType.STRING)
                .setRequired(true)
                .setAllowBlank(false)
                .setMaximumLength(100));
        entityDef.addField(new NumericFieldDef("age", FieldType.INT)
                .setRequired(true)
                .setMinimum(25)
                .setMaximum(75));
        
        Record record = new Record();
        record.addField("name", "");
        record.addField("age", 76);
        
        System.out.println(entityDef.validateRecord(record));
    }

}
