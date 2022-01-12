/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.eventbus;

import java.util.Arrays;

import com.northconcepts.datapipeline.eventbus.Event;
import com.northconcepts.datapipeline.eventbus.EventBus;
import com.northconcepts.datapipeline.eventbus.UntypedEventListener;
import com.northconcepts.datapipeline.examples.userguide.eventbus.IUserListener;
import com.northconcepts.datapipeline.examples.userguide.eventbus.User;

public class UseAnEventBus {

    public static void main(String[] args) throws Throwable {
        EventBus bus = new EventBus();

        // register type-specific event listener 1
        bus.addListener(IUserListener.class, new IUserListener(){
            @Override
            public void userAdded(User user) {
                System.out.println("1: user added " + user);
            }
        });

        // register type-specific event listener 2
        bus.addListener(IUserListener.class, new IUserListener(){
            @Override
            public void userAdded(User user) {
                System.out.println("2: user added " + user);
            }
        });

        // register untyped event listener
        bus.addListener(new UntypedEventListener() {
            
            @Override
            public void onEvent(Event<?> event) {
                System.out.println("3: " + event + " -- " + Arrays.asList(event.getArguments()));
            }
        });

        // get event publisher
        IUserListener publisher = bus.getPublisher(bus, IUserListener.class);

        // publish
        publisher.userAdded(new User("bob@example.com"));
        publisher.userAdded(new User("carol@abc.org"));

        bus.shutdown();

    }
}
