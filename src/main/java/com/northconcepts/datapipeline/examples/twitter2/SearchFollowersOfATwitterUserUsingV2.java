package com.northconcepts.datapipeline.examples.twitter2;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.twitter2.TwitterFollowerReader;


public class SearchFollowersOfATwitterUserUsingV2 {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_SECRET = "YOUR_API_SECRET";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";
    private static final String ACCESS_TOKEN_SECRET = "YOUR_ACCESS_TOKEN_SECRET";

    private static final String USERNAME = "TwitterDev";

    public static void main(String[] args) {
        DataReader reader = new TwitterFollowerReader(API_KEY, API_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET, USERNAME);
        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
