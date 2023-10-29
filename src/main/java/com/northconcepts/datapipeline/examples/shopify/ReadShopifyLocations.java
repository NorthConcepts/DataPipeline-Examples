package com.northconcepts.datapipeline.examples.shopify;


import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.shopify.ShopifyLocationReader;
import com.northconcepts.datapipeline.core.DataReader;

import java.io.File;


public class ReadShopifyLocations {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "access-token";
    
    public static void main(String[] args) {
        DataReader reader = new ShopifyLocationReader(DOMAIN, TOKEN);
        DataWriter writer = new CSVWriter(new File("data/output/orders.csv"));

        Job.run(reader, writer);
    }
    
}
