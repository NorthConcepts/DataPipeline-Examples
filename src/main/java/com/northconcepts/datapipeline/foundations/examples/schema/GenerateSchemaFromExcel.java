package com.northconcepts.datapipeline.foundations.examples.schema;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;

public class GenerateSchemaFromExcel {

    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSF)
                .open(new File("example/data/input/jewelry.xlsx"));

        DataReader reader = new ExcelReader(document).setFieldNamesInFirstRow(true);

        GenerateEntityFromDataset generator = new GenerateEntityFromDataset();
        EntityDef entity = generator.generateEntity(reader);

        SchemaDef schema = new SchemaDef("jewellery")
                .addEntity(entity);

        System.out.println(schema.toXml());
        System.out.println(schema.getSchemaProblems(true));
    }

}
