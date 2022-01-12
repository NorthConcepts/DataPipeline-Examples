/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.LimitReader;
import com.northconcepts.datapipeline.core.SequenceReader;
import com.northconcepts.datapipeline.core.SortingReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.group.GroupByReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.Ngrams;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.xml.XmlRecordReader;

public class ExtractBigramsTrigramsAndNgrams {
    
    private static final int NGRAMS = 3;  // bigram: 2; trigrams: 3; quadrigrams: 4;
    private static final int TOP_PHRASES = 25;
    
    private static final String[] URLS = {
            "https://rss.cbc.ca/lineup/topstories.xml",
            "https://rss.cbc.ca/lineup/world.xml",
            "https://rss.cbc.ca/lineup/canada.xml",
            "https://rss.cbc.ca/lineup/politics.xml",
            "https://rss.cbc.ca/lineup/business.xml",
            "https://rss.cbc.ca/lineup/health.xml",
            "https://rss.cbc.ca/lineup/arts.xml",
            "https://rss.cbc.ca/lineup/technology.xml",
            "https://rss.cbc.ca/lineup/offbeat.xml",
            "https://www.cbc.ca/cmlink/rss-cbcaboriginal",
            
            "https://globalnews.ca/feed/",
            "https://globalnews.ca/canada/feed/",
            "https://globalnews.ca/world/feed/",
            "https://globalnews.ca/politics/feed/",
            "https://globalnews.ca/money/feed/",
            "https://globalnews.ca/health/feed/",
            "https://globalnews.ca/entertainment/feed/",
            "https://globalnews.ca/environment/feed/",
            "https://globalnews.ca/tech/feed/",
            "https://globalnews.ca/sports/feed/",
            
            "https://www.ctvnews.ca/rss/ctvnews-ca-top-stories-public-rss-1.822009",
            "https://www.ctvnews.ca/rss/ctvnews-ca-canada-public-rss-1.822284",
            "https://www.ctvnews.ca/rss/ctvnews-ca-world-public-rss-1.822289",
            "https://www.ctvnews.ca/rss/ctvnews-ca-entertainment-public-rss-1.822292",
            "https://www.ctvnews.ca/rss/ctvnews-ca-politics-public-rss-1.822302",
            "https://www.ctvnews.ca/rss/lifestyle/ctv-news-lifestyle-1.3407722",
            "https://www.ctvnews.ca/rss/business/ctv-news-business-headlines-1.867648",
            "https://www.ctvnews.ca/rss/ctvnews-ca-sci-tech-public-rss-1.822295",
            "https://www.ctvnews.ca/rss/sports/ctv-news-sports-1.3407726",
            "https://www.ctvnews.ca/rss/ctvnews-ca-health-public-rss-1.822299",
            "https://www.ctvnews.ca/rss/autos/ctv-news-autos-1.867636",
            };

    public static void main(String[] args) throws Throwable {
        
        SequenceReader sequenceReader = new SequenceReader();
        
        for (String url : URLS) {
            BufferedReader input = new BufferedReader(new InputStreamReader(new URL(url).openStream(), "UTF-8"));
            sequenceReader.add(new XmlRecordReader(input).addRecordBreak("/rss/channel/item"));
        }
        
        DataReader reader = sequenceReader;
        
        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("title").lowerCase())
                .add(new Ngrams("title", "phrase", NGRAMS));
        
        reader = new GroupByReader(reader, "phrase")
                .setExcludeNulls(true)
                .count("count", true);
        
        reader = new SortingReader(reader).desc("count").asc("phrase");
        
        reader = new LimitReader(reader, TOP_PHRASES);
        
        DataWriter writer = new CSVWriter(new OutputStreamWriter(System.out))
                .setFieldNamesInFirstRow(true);
   
        Job.run(reader, writer);
        
    }
    
}
