package com.northconcepts.datapipeline.examples.userguide.eventbus;

import java.util.EventListener;

public interface IUserListener extends EventListener {
    
    void userAdded(User user);

}
