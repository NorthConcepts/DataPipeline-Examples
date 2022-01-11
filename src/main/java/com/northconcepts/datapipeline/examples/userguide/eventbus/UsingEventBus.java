/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.userguide.eventbus;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.eventbus.Event;
import com.northconcepts.datapipeline.eventbus.EventBus;
import com.northconcepts.datapipeline.eventbus.EventBusLifecycleListener;
import com.northconcepts.datapipeline.eventbus.EventBusReader;
import com.northconcepts.datapipeline.eventbus.EventBusWriter;
import com.northconcepts.datapipeline.eventbus.EventFilter;
import com.northconcepts.datapipeline.eventbus.ExceptionListener;
import com.northconcepts.datapipeline.eventbus.RecordListener;
import com.northconcepts.datapipeline.eventbus.UntypedEventListener;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.xml.SimpleXmlWriter;

public class UsingEventBus {

    
    public static void main1(String[] args) throws Throwable {
        EventBus bus = new EventBus();
        
        // listener 1
        bus.addListener(IUserListener.class, null, new IUserListener(){
            @Override
            public void userAdded(User user) {
                System.out.println("1: user added " + user);
            }
        });

        // listener 2
        bus.addListener(IUserListener.class, null, new IUserListener(){
            @Override
            public void userAdded(User user) {
                System.out.println("2: user added " + user);
            }
        });
        
        // publisher
        IUserListener publisher = bus.getPublisher(bus, IUserListener.class);
        
        // publish
        publisher.userAdded(new User("bob@example.com"));
        publisher.userAdded(new User("carol@abc.org"));
        
        bus.shutdown();

    }

    
    public static void main2(String[] args) throws Throwable {
        EventBus bus = new EventBus();
        
        // listener 1
        bus.addListener(IUserListener.class, null, new IUserListener(){
            @Override
            public void userAdded(User user) {
                Event<?> event = EventBus.getCurrentEvent();
                System.out.println("1: user added " + event.getMethod());
            }
        });

        // listener 2
        bus.addListener(IUserListener.class, null, new IUserListener(){
            @Override
            public void userAdded(User user) {
                System.out.println("2: user added " + user);
            }
        });
        
        // publisher
        IUserListener publisher = bus.getPublisher(bus, IUserListener.class);
        
        // publish
        publisher.userAdded(new User("bob@example.com"));
        publisher.userAdded(new User("carol@abc.org"));
        
        bus.shutdown();

    }

    
    public static void main3(String[] args) throws Throwable {
//        DataEndpoint.enableJmx();
        
        DataReader reader ;
        DataWriter writer;

        
        final String PURCHASES_TOPIC = "purchases";

        EventBus bus = new EventBus().setName("App1 Bus");
        
        
        // read bus, write purchases to console
        reader = new EventBusReader(bus, PURCHASES_TOPIC); 
        writer = new StreamWriter(System.out);
        new Job(reader, writer).runAsync();
        
        // read bus, write purchases to file
        reader = new EventBusReader(bus, PURCHASES_TOPIC); 
        writer = new SimpleXmlWriter(new File("example/data/output/purchases.xml"))
                .setPretty(true);
        new Job(reader, writer).runAsync();
        
        
        // write purchases to bus
        reader = new CSVReader(new File("example/data/input/purchases.csv"))
                .setFieldNamesInFirstRow(true);
        writer = new EventBusWriter(bus, PURCHASES_TOPIC);
        new Job(reader, writer).run();

        bus.shutdown();
        
        
//        System.out.println("before shutdown"); 
////        bus.shutdown();
//        System.out.println("after shutdown"); 
    }


    public static void main4(String[] args) throws Throwable {
        EventBusLifecycleListener listener = new EventBusLifecycleListener() {
            
            @Override
            public void onStartUp(EventBus bus) {
                System.out.println("*****  bus started: " + bus.getName());
            }
            
            @Override
            public void onShuttingDown(EventBus bus) {
                System.out.println("*****  bus finished: " + bus.getName());
            }
        };
        
        EventBus.addEventBusLifecycleListener(listener);
        
        EventBus bus1 = new EventBus().setName("first bus");
        EventBus bus2 = new EventBus().setName("second bus");
        
        Thread.sleep(1000L);
        
        bus1.shutdown();
        bus2.shutdown();
  }

    
    public static void main7(String[] args) throws Throwable {
//      DataEndpoint.enableJmx();
      
      DataReader reader ;
      DataWriter writer;

      
      EventBus bus = new EventBus();
      
      
      // Option 1 - reader monitor
      reader = new EventBusReader(bus)
              .setStopOnWriterEOF(false);  // prevent this reader from stopping when a writer terminates 
      writer = new StreamWriter(System.out);
      new Job(reader, writer).runAsync();
      
      // Option 2 - callback monitor
      bus.addListener(RecordListener.class, null, new RecordListener() {

            @Override
            public void onRecord(Record record) {
                System.out.println(record);
            }
      });
      
      // write purchases to the bus under the "purchases" topic
      reader = new CSVReader(new File("example/data/input/purchases.csv"))
              .setFieldNamesInFirstRow(true);
      writer = new EventBusWriter(bus, "purchases");
      Job job1 = new Job(reader, writer).runAsync();
      
      // write calls to the bus under the "calls" topic
      reader = new CSVReader(new File("example/data/input/call-center-inbound-call.csv"))
              .setFieldNamesInFirstRow(true);
      writer = new EventBusWriter(bus, "calls");
      Job job2 = new Job(reader, writer).runAsync();
      
      job1.waitUntilFinished(); 
      job2.waitUntilFinished(); 
      

      bus.shutdown();
      
      
//      System.out.println("before shutdown"); 
////      bus.shutdown();
//      System.out.println("after shutdown"); 
  }


    public static void main8(String[] args) throws Throwable {
//      DataEndpoint.enableJmx();
      
      EventBus bus = EventBus.getSystemEventBus();
      
      bus.addListener((EventFilter)null, new UntypedEventListener() {
        
        @Override
        public void onEvent(Event<?> event) {
            System.out.println("event: " + event);
        }
      });
      
      DataReader reader ;
      DataWriter writer;

      // write purchases to the bus under the "purchases" topic
      reader = new CSVReader(new File("example/data/input/purchases.csv"))
              .setFieldNamesInFirstRow(true);
      writer = new EventBusWriter(bus, "purchases");
      Job job1 = new Job(reader, writer).runAsync();
      
      // write calls to the bus under the "calls" topic
      reader = new CSVReader(new File("example/data/input/call-center-inbound-call.csv"))
              .setFieldNamesInFirstRow(true);
      writer = new EventBusWriter(bus, "calls");
      Job job2 = new Job(reader, writer).runAsync();
      
      job1.waitUntilFinished(); 
      job2.waitUntilFinished(); 

      bus.shutdown();
      
      
//      System.out.println("before shutdown"); 
////      bus.shutdown();
//      System.out.println("after shutdown"); 
  }


    public static void main(String[] args) throws Throwable {
        EventBus bus = new EventBus();

        // watch for exceptions
        bus.addExceptionListener(null, new ExceptionListener() {

            @Override
            public void onException(Event<?> event, Throwable exception, Object targetListener) {
                System.out.println("exception:  " + exception);
                System.out.println("    event:  " + event);
                System.out.println("    targetListener:  " + targetListener);
            }
        });

        // produce an exception
        bus.addListener(RecordListener.class, null, new RecordListener() {

            @Override
            public void onRecord(Record record) {
                throw new RuntimeException("A random failure");
            }
        });
        
        // write purchases to the bus under the "purchases" topic
        DataReader reader = new CSVReader(new File("example/data/input/purchases.csv")).setFieldNamesInFirstRow(true);
        DataWriter writer = new EventBusWriter(bus, "purchases");
        new Job(reader, writer).runAsync().waitUntilFinished();

        bus.shutdown();      
  }


}
