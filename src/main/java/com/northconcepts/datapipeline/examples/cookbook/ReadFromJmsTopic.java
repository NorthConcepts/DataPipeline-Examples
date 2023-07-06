package com.northconcepts.datapipeline.examples.cookbook;

import java.util.List;
import java.util.Properties;

import javax.naming.Context;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.jms.JmsDestinationType;
import com.northconcepts.datapipeline.jms.JmsReader;
import com.northconcepts.datapipeline.jms.JmsSettings;
import com.northconcepts.datapipeline.jms.JmsWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;

public class ReadFromJmsTopic {
    
    // run activemq before running the code below
    public static void main(String[] args) throws Exception {
        Record record = new Record();
        record.setField("Team", "Manchester United");
        record.setField("Points", 90);
        
        RecordList recordList = new RecordList(record);
        MemoryReader memoryReader = new MemoryReader(recordList);
        
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        
        DataReader reader = new JmsReader(new JmsSettings("Premier League", JmsDestinationType.TOPIC, props));
        
        MemoryWriter memoryWriter = new MemoryWriter();
        Job asyncJob = new Job(reader, memoryWriter);
        asyncJob.runAsync();
        
        Thread.sleep(5000); // allow JmsReader time to be registered as subscriber
        
        DataWriter writer = new JmsWriter(new JmsSettings("Premier League", JmsDestinationType.TOPIC, props));
        Job.run(memoryReader, writer);
        
        Thread.sleep(5000); // allow JmsReader time to read
        
        asyncJob.cancel();
        
        List<Record> records = memoryWriter.getRecordList().getRecords();
        System.out.println("List of Records: " + records);
        
        System.exit(0);
    }
/* output
List of Records: [Record (MODIFIED) {
    0:[Points]:INT=[90]:Integer
    1:[Team]:STRING=[Manchester United]:String
    2:[jms_message_id]:STRING=[ID:asus_k43s-50726-1483620205667-3:1:1:1:1]:String
    3:[jms_timestamp]:LONG=[1483620210454]:Long
    4:[jms_correlation_id_as_bytes]:UNDEFINED=[null]
    5:[jms_correlation_id]:UNDEFINED=[null]
    6:[jms_reply_to]:UNDEFINED=[null]
    7:[jms_destination]:STRING=[topic://Premier League]:String
    8:[jms_delivery_mode]:INT=[2]:Integer
    9:[jms_redelivered]:BOOLEAN=[false]:Boolean
    10:[jms_type]:UNDEFINED=[null]
    11:[jms_expiration]:LONG=[0]:Long
    12:[jms_priority]:INT=[4]:Integer
}
]
*/
}
