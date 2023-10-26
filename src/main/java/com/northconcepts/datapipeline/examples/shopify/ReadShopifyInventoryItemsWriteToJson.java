package com.northconcepts.datapipeline.examples.shopify;

import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineOutput;
import com.northconcepts.datapipeline.foundations.pipeline.input.DataReaderPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.pipeline.output.JsonRecordPipelineOutput;
import com.northconcepts.datapipeline.shopify.ShopifyInventoryItemReader;
import com.northconcepts.datapipeline.shopify.ShopifyInventoryLevelReader;
import com.northconcepts.datapipeline.shopify.client.ShopifyInventoryLevelCriteria;

public class ReadShopifyInventoryItemsWriteToJson {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "store-token";
    
    public static void main(String[] args) {
        PipelineInput pipelineInput = new DataReaderPipelineInput(() ->
            new ShopifyInventoryItemReader(DOMAIN, TOKEN)
                .setIds("49129939697963", "49129939730731", "49129939763499", "49129939796267", "49129939829035"));

        PipelineOutput pipelineOutput = new JsonRecordPipelineOutput().setPretty(true)
            .setFileSink(new LocalFileSink().setPath("data/output/output.json"));

        Pipeline pipeline = new Pipeline()
            .setInput(pipelineInput)
            .setOutput(pipelineOutput);

        pipeline.run();
    }
    
}
