package com.northconcepts.datapipeline.examples.shopify;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.input.DataReaderPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.shopify.ShopifyInventoryLevelReader;
import com.northconcepts.datapipeline.shopify.client.ShopifyInventoryLevelCriteria;

import java.io.File;

public class ReadShopifyInventoryLevels {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "access-token";
    
    public static void main(String[] args) {
        DataReader reader = new ShopifyInventoryLevelReader(DOMAIN, TOKEN)
            .setInventoryLevelCriteria(new ShopifyInventoryLevelCriteria()
                .setLocationIds("92972351787")
                .setInventoryItemIds("49129939829035", "49129939796267", "49129939763499"));

        ExcelDocument document = new ExcelDocument();
        DataWriter writer = new ExcelWriter(document).setFieldNamesInFirstRow(true);

        Job.run(reader, writer);

        document.save(new File("data/output/inventory-levels.xls"));
    }
    
}
