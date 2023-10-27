package com.northconcepts.datapipeline.examples.shopify;

import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.input.DataReaderPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.shopify.ShopifyInventoryLevelReader;
import com.northconcepts.datapipeline.shopify.client.ShopifyInventoryLevelCriteria;

public class ReadShopifyInventoryLevels {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "store-token";
    
    public static void main(String[] args) {
        PipelineInput pipelineInput = new DataReaderPipelineInput(() ->
            new ShopifyInventoryLevelReader(DOMAIN, TOKEN)
                .setInventoryLevelCriteria(new ShopifyInventoryLevelCriteria()
                    .setLocationIds("92972351787")
                    .setInventoryItemIds("49129939829035", "49129939796267", "49129939763499")));

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
            .setFileSink(new LocalFileSink().setPath("data/output/output.xlsx"))
            .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline()
            .setInput(pipelineInput)
            .setOutput(pipelineOutput);

        pipeline.run();
    }
    
}
