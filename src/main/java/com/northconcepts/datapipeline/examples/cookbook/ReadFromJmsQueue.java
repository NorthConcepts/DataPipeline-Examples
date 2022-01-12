/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.util.Properties;

import javax.naming.Context;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.jms.JmsDestinationType;
import com.northconcepts.datapipeline.jms.JmsReader;
import com.northconcepts.datapipeline.jms.JmsSettings;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromJmsQueue {
    
    // run activemq before running the code below
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        
        DataReader reader = new JmsReader(new JmsSettings("premierLeague", JmsDestinationType.QUEUE, props))
                .setReceiveTimeout(4000L);
        
        Job.run(reader, new StreamWriter(System.out));
    }
/* output
-----------------------------------------------
0 - Record (MODIFIED) {
    0:[Points]:INT=[90]:Integer
    1:[Team]:STRING=[Manchester United]:String
    2:[jms_message_id]:STRING=[ID:asus_k43s-50319-1483615879678-1:1:1:1:1]:String
    3:[jms_timestamp]:LONG=[1483615880082]:Long
    4:[jms_correlation_id_as_bytes]:UNDEFINED=[null]
    5:[jms_correlation_id]:UNDEFINED=[null]
    6:[jms_reply_to]:UNDEFINED=[null]
    7:[jms_destination]:STRING=[queue://premierLeague]:String
    8:[jms_delivery_mode]:INT=[2]:Integer
    9:[jms_redelivered]:BOOLEAN=[false]:Boolean
    10:[jms_type]:UNDEFINED=[null]
    11:[jms_expiration]:LONG=[0]:Long
    12:[jms_priority]:INT=[4]:Integer
}

-----------------------------------------------
1 records
*/
}
