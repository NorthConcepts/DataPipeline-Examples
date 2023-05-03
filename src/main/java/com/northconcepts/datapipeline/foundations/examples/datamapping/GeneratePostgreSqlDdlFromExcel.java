package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;
import com.northconcepts.datapipeline.sql.postgresql.CreatePostgreSqlDdlFromSchemaDef;

public class GeneratePostgreSqlDdlFromExcel {

    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument()
                .open(new File("example/data/input/call-center-inbound-call.xlsx"));

        DataReader reader = new ExcelReader(document).setFieldNamesInFirstRow(true);

        GenerateEntityFromDataset generator = new GenerateEntityFromDataset();
        EntityDef entity = generator.generateEntity(reader).setName("callingInfo");

        SchemaDef schema = new SchemaDef("callCenter")
                .addEntity(entity);

        CreatePostgreSqlDdlFromSchemaDef postgresDdl = new CreatePostgreSqlDdlFromSchemaDef(schema)
                .setPretty(true);

        System.out.println(postgresDdl);
    }
    
}
