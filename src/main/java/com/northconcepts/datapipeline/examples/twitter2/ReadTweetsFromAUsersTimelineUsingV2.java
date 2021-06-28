/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.twitter2;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.twitter2.TwitterTimelineTweetsReader;

public class ReadTweetsFromAUsersTimelineUsingV2 {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_SECRET = "YOUR_API_SECRET";
    private static final String BEARER_TOKEN = "YOUR_BEARER_TOKEN";

    public static void main(String[] args) {

        TwitterTimelineTweetsReader reader = new TwitterTimelineTweetsReader(API_KEY, API_SECRET, BEARER_TOKEN, "TwitterDev");
        reader.setMaxResults(100);

        Job.run(reader, new StreamWriter(System.out));
    }
}
