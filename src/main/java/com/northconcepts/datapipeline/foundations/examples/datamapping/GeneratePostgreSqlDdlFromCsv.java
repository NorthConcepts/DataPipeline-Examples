package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;
import com.northconcepts.datapipeline.sql.postgresql.CreatePostgreSqlDdlFromSchemaDef;

import java.io.File;

public class GeneratePostgreSqlDdlFromCsv {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        GenerateEntityFromDataset generator = new GenerateEntityFromDataset();
        EntityDef entity = generator.generateEntity(reader).setName("creditBalance");

        SchemaDef schema = new SchemaDef("credit-balance-01")
                .addEntity(entity);

        CreatePostgreSqlDdlFromSchemaDef postgresDdl = new CreatePostgreSqlDdlFromSchemaDef(schema)
                .setPretty(true)
                .setCheckIfDropTableExists(false);

        System.out.println(postgresDdl);
    }
    
}
