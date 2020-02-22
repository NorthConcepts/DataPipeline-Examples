package com.northconcepts.datapipeline.foundations.examples.eventbus;

import com.northconcepts.datapipeline.eventbus.EventBus;
import com.northconcepts.datapipeline.examples.userguide.eventbus.IUserListener;
import com.northconcepts.datapipeline.examples.userguide.eventbus.User;

public class UseAnEventBus {

    public static void main(String[] args) throws Throwable {
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
}
