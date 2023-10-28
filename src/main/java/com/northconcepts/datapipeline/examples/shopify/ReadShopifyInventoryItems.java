package com.northconcepts.datapipeline.examples.shopify;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineOutput;
import com.northconcepts.datapipeline.foundations.pipeline.input.DataReaderPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.JsonRecordPipelineOutput;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonRecordWriter;
import com.northconcepts.datapipeline.shopify.ShopifyInventoryItemReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ReadShopifyInventoryItems {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "store-token";
    
    public static void main(String[] args) throws FileNotFoundException {
        DataReader reader = new ShopifyInventoryItemReader(DOMAIN, TOKEN)
            .setIds("49129939697963", "49129939730731", "49129939763499", "49129939796267", "49129939829035");

        File file = new File("data/output/inventory-items.json");
        DataWriter writer = new JsonRecordWriter(new OutputStreamWriter(new FileOutputStream(file))).setPretty(true);

        Job.run(reader, writer);
    }
    
}
