package com.northconcepts.datapipeline.foundations.examples.schema;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;

public class GenerateSchemaFromParquet {

    public static void main(String[] args) {
        DataReader reader = new ParquetDataReader(new File("example/data/input/read_parquet_file.parquet"));

        GenerateEntityFromDataset generator = new GenerateEntityFromDataset();

        EntityDef entity = generator.generateEntity(reader);

        SchemaDef schema = new SchemaDef("schema")
                .addEntity(entity);

        System.out.println(schema.toXml());
        System.out.println(schema.getSchemaProblems(true));
    }

}
