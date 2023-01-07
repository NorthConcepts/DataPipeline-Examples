/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.util.Map.Entry;
import java.util.concurrent.atomic.LongAdder;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.time.DateTimePattern;

public class ShowColumnStatistics {

    public static void main(String[] args) {

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFileSource().setPath("data/input/Listing.csv"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline();

        pipeline.setInput(pipelineInput);

        Dataset dataset = new MemoryDataset(pipeline);
        dataset.load().waitForColumnStatsToLoad();

        System.out.println("Column Count: " + dataset.getColumnCount());
        System.out.println("Record Count: " + dataset.getRecordCount());

        for(Column column : dataset.getColumns()) {
            System.out.println("Name: " + column.getName());
            System.out.println("Value Count: " + column.getValueCount());
            System.out.println("Null Count: " + column.getNullCount());
            System.out.println("Non Null Count: " + column.getNonNullCount());
            System.out.println("Blank Count: " + column.getBlankCount());
            System.out.println("Non Null Non Blank Count: " + column.getNonNullNonBlankCount());
            System.out.println("Unique Value Count: " + column.getUniqueValueCount());

            System.out.println("Is Numeric Column: " + column.getNumeric());
            if (column.getNumeric()) {
                System.out.println("    Inferred Numeric Value Count: " + column.getInferredNumericValueCount());
                System.out.println("    " + column.getNumberDescriptor() + " -- " + column.getNumberDescriptor().getFieldType());
            }

            System.out.println("Is Temporal Column: " + column.getTemporal());
            if (column.getTemporal()) {
                System.out.println("    Inferred Temporal Value Count: " + column.getInferredTemporalValueCount());
                for (Entry<DateTimePattern, LongAdder> entry : column.getTemporalPatterns().entrySet()) {
                    System.out.println("    " + entry.getKey().getPattern() + "  --  " + entry.getValue().longValue());
                }
            }

            System.out.println("Is Boolean Column: " + column.getBoolean());
            System.out.println("    Inferred Boolean Value Count: " + column.getInferredBooleanValueCount());
            System.out.println("Is Array Column: " + column.isArray());
            if(column.isArray()){
                System.out.println("    Array Value Count: " + column.getArrayValueCount());
                System.out.println("    Minimum Array Elements: " + column.getMinimumArrayElements());
                System.out.println("    Maximum Array Elements: " + column.getMaximumArrayElements());
            }
            System.out.println("Minimum Length: " + column.getMinimumLength());
            System.out.println("Maximum Length: " + column.getMaximumLength());
            System.out.println("Length Sum: " + column.getLengthSum());
            System.out.println("Average Length: " + column.getAverageLength());
            System.out.println("Sample Value: " + column.getSampleValue());
            System.out.println("Inferred Type Name: " + column.getInferredTypeName());
            System.out.println("Inferred Field Type: " + column.getInferredFieldType());
            System.out.println("Field Type: " + column.getFieldType());
            System.out.println("Field Type Count: " + column.getFieldTypeCount());
            System.out.println("Best Fit Field Type: " + column.getBestFitFieldType());
            for (Entry<FieldType, LongAdder> entry : column.getFieldTypes().entrySet()) {
                System.out.println("    " + entry.getKey() + "  --  " + entry.getValue().longValue());
            }
            System.out.println("===============================");

        }

    }
}
