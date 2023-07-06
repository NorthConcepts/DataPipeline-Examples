package com.northconcepts.datapipeline.examples.twitter;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.twitter.TwitterFilterStreamReader;
import com.northconcepts.datapipeline.twitter.TwitterStreamReader;

public class FilterTwitterInRealTime {

    public static void main(String[] args) {
        
        // Your Twitter API keys and secret
        final String CONSUMER_KEY = "**********";
        final String CONSUMER_SECRET = "**********";
        final String ACCESS_KEY = "**********";
        final String ACCESS_TOKEN_SECRET = "**********";

        TwitterStreamReader reader = new TwitterFilterStreamReader(
                CONSUMER_KEY, CONSUMER_SECRET, ACCESS_KEY, ACCESS_TOKEN_SECRET)
                .setTrack("#ManUtd", "#ManchesterUnited");

        Job.run(reader,new StreamWriter(System.out));
    }
}
