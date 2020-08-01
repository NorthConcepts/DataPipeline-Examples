/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.blog;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.LimitReader;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.SortingReader;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.twitter.ApiLimitPolicy;
import com.northconcepts.datapipeline.twitter.EntityExpansionPolicy;
import com.northconcepts.datapipeline.twitter.TwitterSearchReader;

public class AggregateTwitterStream {

//    private static final int MAX_RESULTS = 30000;
//    private static final int MAX_RESULTS = 40000;
    private static final int MAX_RESULTS = 100;
    private static final String CONSUMER_KEY = "CONSUMER_KEY-XXX";
    private static final String CONSUMER_SECRET = "CONSUMER_SECRET-XXX";

//     private static final String ACCESS_KEY = SearchTwitter.ACCESS_KEY;
//     private static final String ACCESS_TOKEN_SECRET = SearchTwitter.ACCESS_TOKEN_SECRET;

    public static void main(String[] args) throws Throwable {

        // Load tweets into memory
        RecordList tweets = new RecordList();
        DataReader reader = new TwitterSearchReader(CONSUMER_KEY, CONSUMER_SECRET, "#Java", MAX_RESULTS)
            .setApiLimitPolicy(ApiLimitPolicy.STOP)
            .setEntityExpansionPolicy(EntityExpansionPolicy.TO_ARRAY);
        DataWriter writer = new MemoryWriter(tweets);
        Job.run(reader, writer);

        // write tweets to first tab in Excel doc
        reader = new MemoryReader(tweets);
        ExcelDocument document = new ExcelDocument();
        writer = new ExcelWriter(document).setSheetName("tweets").setAutofitColumns(true).setAutoFilterColumns(true);
        Job.run(reader, writer);

        // write counts to subsequent tabs in Excel doc
        count(tweets, document, "HashtagEntities");
        count(tweets, document, "UserMentionEntities");
        count(tweets, document, "URLEntities");
        count(tweets, document, "UserScreenName", "UserFollowersCount", "UserFollowingCount", 
                "UserTweets", "UserFavouritesCount", "UserLocation", "UserLang", "UserTimeZone", 
                "UserUtcOffset", "UserCreatedAt", "UserURL", "UserDescription");
        
        top(tweets, document, 10, "RetweetCount", "FavoriteCount", "Id", "HashtagEntities", 
                "UserMentionEntities", "URLEntities", "UserScreenName", "UserFollowersCount", "UserFollowingCount", 
                "UserTweets", "UserFavouritesCount", "UserLocation", "UserURL", "UserDescription");
                
        top(tweets, document, 10, "FavoriteCount", "RetweetCount", "Id", "HashtagEntities",
                "UserMentionEntities", "URLEntities", "UserScreenName", "UserFollowersCount", "UserFollowingCount", 
                "UserTweets", "UserFavouritesCount", "UserLocation", "UserURL", "UserDescription");
                
        String[] sheetNames = document.getSheetNames();
        for (String sheetName : sheetNames) {
            System.out.println("Sheet: "  + sheetName);
        }

        document.save(new File("example/data/output/twitter.xlsx"));

        System.out.println("Done.");
    }

    private static void count(RecordList tweets, ExcelDocument document, String countField, String ... firstFields) {
        DataReader reader = new MemoryReader(tweets);
        
        reader = new TransformingReader(reader).add(new BasicFieldTransformer(countField).lowerCase());
        
        GroupByReader groupByReader = new GroupByReader(reader, countField)
            .setExcludeNulls(true)
            .count("count", true);
        if (firstFields != null) {
            for (String firstField : firstFields) {
                groupByReader.first(firstField, firstField, false);
            }
        }
        reader = groupByReader;
        reader = new SortingReader(reader).desc("count").asc(countField);

        DataWriter writer = new ExcelWriter(document)
            .setSheetName(countField)
            .setAutofitColumns(true)
            .setAutoFilterColumns(firstFields != null && firstFields.length > 0);

        Job.run(reader, writer);
    }

    private static void top(RecordList tweets, ExcelDocument document, int topCount, String topField, String ... firstFields) {
        DataReader reader = new MemoryReader(tweets);
        
        GroupByReader groupByReader = new GroupByReader(reader, "Text")
            .setExcludeNulls(true)
            .max(topField, topField, true);
        if (firstFields != null) {
            for (String firstField : firstFields) {
                groupByReader.first(firstField, firstField, false);
            }
        }
        reader = groupByReader;
        
        reader = new SortingReader(reader).desc(topField).asc("Text");
        reader = new LimitReader(reader, topCount);
        reader = new TransformingReader(reader).add(new SelectFields().add(topField).add("Text").add(firstFields));
        
        DataWriter writer = new ExcelWriter(document)
            .setSheetName(topField)
            .setAutofitColumns(true)
            .setAutoFilterColumns(firstFields != null && firstFields.length > 0);

        Job.run(reader, writer);
    }

}
