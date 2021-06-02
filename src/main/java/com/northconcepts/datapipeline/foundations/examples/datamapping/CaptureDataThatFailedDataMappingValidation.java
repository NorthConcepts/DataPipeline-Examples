package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;

import java.io.File;
import java.math.BigDecimal;

public class CaptureDataThatFailedDataMappingValidation {

    public static void main(String... args) {
        EntityDef targetEntity = new EntityDef()
            .addField(new NumericFieldDef("Price", FieldType.DOUBLE).setRequired(true).setMinimum(50));

        DataMapping mapping = new DataMapping()
            .setTargetEntity(targetEntity)
            .setValue("Markup", BigDecimal.valueOf(10.00))  // add a constant to be used in the mapping
            .addFieldMapping(new FieldMapping("Title", "coalesce(source.Title, source.Handle)"))
            .addFieldMapping(new FieldMapping("Cost", "${source.Variant Price}"))  // use ${} since field has space in name
            .addFieldMapping(new FieldMapping("Price", "toBigDecimal(target.Cost) + Markup"));

        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
            .setAllowMultiLineText(true)
            .setFieldNamesInFirstRow(true);

        MemoryWriter discardWriter = new MemoryWriter();

        reader = new DataMappingReader(reader, mapping, discardWriter, "ErrorMessage");

        Job.run(reader, new NullWriter()); //run mapping
        Job.run(new MemoryReader(discardWriter.getRecordList()), StreamWriter.newSystemOutWriter()); //print discarded records
    }
}
