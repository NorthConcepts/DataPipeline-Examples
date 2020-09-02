/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.userguide.eventbus;

import java.util.EventListener;

import com.northconcepts.datapipeline.examples.userguide.eventbus.User;

public interface IUserListener extends EventListener {
    
    void userAdded(User user);

}
