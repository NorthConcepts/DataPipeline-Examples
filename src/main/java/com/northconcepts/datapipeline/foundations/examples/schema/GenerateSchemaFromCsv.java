package com.northconcepts.datapipeline.foundations.examples.schema;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;

public class GenerateSchemaFromCsv {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/patient-visits-raw-10000.csv"))
                .setFieldNamesInFirstRow(true);

        GenerateEntityFromDataset generator = new GenerateEntityFromDataset()
                .setEntityName("patient-visits")
                .setMinRecords(1000L)
                .setMaxWaitTimeMillis(10_000L);

        EntityDef entity = generator.generateEntity(reader);
        SchemaDef schema = new SchemaDef("patient")
                .addEntity(entity);

        System.out.println(schema.toXml());
        System.out.println(schema.getSchemaProblems(true));
    }

}
