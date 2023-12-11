package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;
import com.northconcepts.datapipeline.sql.mysql.CreateMySqlDdlFromSchemaDef;

import java.io.File;

public class GenerateMySqlDdlFromCsv {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/1000_Sales_Records.csv"))
                .setFieldNamesInFirstRow(true);

        GenerateEntityFromDataset generator = new GenerateEntityFromDataset();
        EntityDef entity = generator.generateEntity(reader).setName("sales");

        SchemaDef schema = new SchemaDef("marketdb")
                .addEntity(entity);

        CreateMySqlDdlFromSchemaDef mySqlDdl = new CreateMySqlDdlFromSchemaDef(schema)
                .setPretty(true)
                .setCheckIfDropTableExists(false);

        System.out.println(mySqlDdl);
    }

}
