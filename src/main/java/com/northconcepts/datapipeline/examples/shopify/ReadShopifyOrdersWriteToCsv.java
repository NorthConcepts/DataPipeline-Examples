package com.northconcepts.datapipeline.examples.shopify;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.shopify.ShopifyOrderReader;
import com.northconcepts.datapipeline.shopify.client.FinancialStatus;
import com.northconcepts.datapipeline.shopify.client.FulfillmentStatus;
import com.northconcepts.datapipeline.shopify.client.OrderStatus;
import com.northconcepts.datapipeline.shopify.client.ShopifyOrder;

import java.io.File;

public class ReadShopifyOrdersWriteToCsv {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "store-token";
    
    public static void main(String[] args) {
        DataReader reader = new ShopifyOrderReader(DOMAIN, TOKEN)
            .setOrderCriteria(new ShopifyOrder()
                .setStatus(OrderStatus.ANY)
                .setFinancialStatus(FinancialStatus.PAID)
                .setFulfillmentStatus(FulfillmentStatus.ANY)
                .setFields("id", "created_at", "checkout_id", "checkout_token"));

        DataWriter writer = new CSVWriter(new File("example/data/output/shopify-orders.csv"));
        
        Job.run(reader, writer);
    }
    
}
