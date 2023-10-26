package com.northconcepts.datapipeline.examples.shopify;

import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineOutput;
import com.northconcepts.datapipeline.foundations.pipeline.input.DataReaderPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.CsvPipelineOutput;
import com.northconcepts.datapipeline.shopify.ShopifyLocationReader;


public class ReadShopifyLocationsWriteToCsv {

    private static final String DOMAIN = "https://your-store-domain.com";
    private static final String TOKEN = "store-token";
    
    public static void main(String[] args) {

        PipelineInput pipelineInput = new DataReaderPipelineInput(() -> new ShopifyLocationReader(DOMAIN, TOKEN));

        PipelineOutput pipelineOutput = new CsvPipelineOutput().setFileSink(new LocalFileSink().setPath("data/output/output.csv"))
            .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline()
            .setInput(pipelineInput)
            .setOutput(pipelineOutput);

        pipeline.run();
    }
    
}
