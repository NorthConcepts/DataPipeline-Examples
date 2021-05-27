package com.northconcepts.datapipeline.examples.twitter2;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.twitter2.TwitterSearchReader;


public class SearchTwitterForTweets {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_SECRET = "YOUR_API_SECRET";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";
    private static final String ACCESS_TOKEN_SECRET = "YOUR_ACCESS_TOKEN_SECRET";
    private static final String SEARCH_KEYWORD = "Corona";

    public static void main(String[] args) {
        DataReader reader = new TwitterSearchReader(API_KEY, API_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET, SEARCH_KEYWORD);
        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}
