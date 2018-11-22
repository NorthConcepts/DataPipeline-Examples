/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.FileWriter;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.xml.SimpleXmlWriter;

public class WriteToXmlStream {
	
	
/* Produces the following XML (line breaks added for clarity)
  
<?xml version="1.0" ?>
<records>
    <record>
        <field name="stageName">John Wayne</field>
        <field name="realName">Marion Robert Morrison</field>
        <field name="gender">male</field>
        <field name="city">Winterset</field>
        <field name="balance">156.35</field>
    </record>
    <record>
        <field name="stageName">Spiderman</field>
        <field name="realName">Peter Parker</field>
        <field name="gender">male</field>
        <field name="city">New York</field>
        <field name="balance">-0.96</field>
    </record>
</records>

*/

    public static void main(String[] args) throws Throwable {

        Record record1 = new Record();
        record1.getField("stageName", true).setValue("John Wayne");
        record1.getField("realName", true).setValue("Marion Robert Morrison");
        record1.getField("gender", true).setValue("male");
        record1.getField("city", true).setValue("Winterset");
        record1.getField("balance", true).setValue(156.35);

        Record record2 = new Record();
        record2.getField("stageName", true).setValue("Spiderman");
        record2.getField("realName", true).setValue("Peter Parker");
        record2.getField("gender", true).setValue("male");
        record2.getField("city", true).setValue("New York");
        record2.getField("balance", true).setValue(-0.96);
               
        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));
        
        SimpleXmlWriter writer = new SimpleXmlWriter(new FileWriter("example/data/output/credit-balance-05.xml"));
        
        Job.run(reader, writer);
    }

}
