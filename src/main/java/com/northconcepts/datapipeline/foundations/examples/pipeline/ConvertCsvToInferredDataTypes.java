package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;

import java.io.File;

public class ConvertCsvToInferredDataTypes {
    public static void main(String[] args) {

        Pipeline pipeline = new Pipeline();
        try (Dataset dataset = new MemoryDataset(pipeline)) {

            pipeline.setInputAsDataReaderFactory(() -> new CSVReader(new File("example/data/input/mix-types.csv")).setFieldNamesInFirstRow(true));
            dataset.load().waitForColumnStatsToLoad();

            GenerateEntityFromDataset generateEntityFromDataset = new GenerateEntityFromDataset();
            generateEntityFromDataset.setFieldTypeSelectionMode(GenerateEntityFromDataset.FieldTypeSelectionMode.BEST_FIT_INFERRED);
            EntityDef entity = generateEntityFromDataset.createEntity(dataset);
            pipeline.setTargetEntity(entity);

            pipeline.setOutputAsDataWriterFactory(() -> StreamWriter.newSystemOutWriter());
            pipeline.run();
        }
    }
}
