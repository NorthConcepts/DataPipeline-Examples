package com.northconcepts.datapipeline.examples.twitter2;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.twitter2.TwitterFilterStreamReader;


public class FilterTwitterInRealTimeUsingV2 {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_SECRET = "YOUR_API_SECRET";

    private static final String BEARER_TOKEN = "YOUR_BEARER_TOKEN";

    public static void main(String[] args) {
        TwitterFilterStreamReader reader = new TwitterFilterStreamReader(API_KEY, API_SECRET, BEARER_TOKEN);
        reader.addRule("cat has:images", "cat images");

        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
