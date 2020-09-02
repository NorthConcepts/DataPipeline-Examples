/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.util.Properties;

import javax.naming.Context;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.jms.JmsDestinationType;
import com.northconcepts.datapipeline.jms.JmsSettings;
import com.northconcepts.datapipeline.jms.JmsWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteToJmsTopic {
    
    // run activemq before running the code below
    public static void main(String[] args) {
        Record record = new Record();
        record.setField("Team", "Manchester United");
        record.setField("Points", 90);
        
        RecordList recordList = new RecordList(record);
        MemoryReader memoryReader = new MemoryReader(recordList);
        
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        
        DataWriter writer = new JmsWriter(new JmsSettings("Premier League", JmsDestinationType.TOPIC, props));
        Job.run(memoryReader, writer);
    }

}
