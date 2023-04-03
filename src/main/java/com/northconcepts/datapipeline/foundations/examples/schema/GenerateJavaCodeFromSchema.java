package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.core.Attributes;
import com.northconcepts.datapipeline.foundations.core.Tags;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.foundations.schema.IndexDef;
import com.northconcepts.datapipeline.foundations.schema.EntityRelationshipDef;
import com.northconcepts.datapipeline.foundations.schema.RelationshipCardinality;
import com.northconcepts.datapipeline.foundations.schema.ForeignKeyAction;
import com.northconcepts.datapipeline.foundations.schema.PrimaryKeyType;
import com.northconcepts.datapipeline.sourcecode.CodeWriter;
import com.northconcepts.datapipeline.sourcecode.JavaCodeBuilder;

public class GenerateJavaCodeFromSchema {

    public static void main(String[] args) {

        JavaCodeBuilder code = new JavaCodeBuilder();
        CodeWriter codeWriter = code.getSourceWriter();
        codeWriter.println("public class DataPipelineExample {");
        codeWriter.println();
        codeWriter.indent();
        codeWriter.println("public static void main(String[] args) throws Throwable {");
        codeWriter.indent();

        SchemaDef schema = createSchema();

        schema.generateJavaCode(code);

        // Adding semicolon to avoid compilation errors.
        codeWriter.println(";");

        codeWriter.outdent();
        codeWriter.println("}");
        codeWriter.outdent();
        codeWriter.println("}");

        System.out.println(code.getSource());

    }
    
    public static SchemaDef createSchema(){
        SchemaDef schema = new SchemaDef()
                .setName("Code Generation")
                .setDescription("Schema description")
                .setAttributes(new Attributes().setValue("key", "value"))
                .setTags(new Tags().add("schema").add("dpf"));

        schema.addEntity(new EntityDef("suppliers")
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG).setPrimaryKeyPosition(1).setPrimaryKeyType(PrimaryKeyType.IDENTITY))
                .addField(new TextFieldDef("supplier_name", FieldType.STRING))
                .addField(new TextFieldDef("city", FieldType.STRING).setStrictArrays(false))
                .addField(new TextFieldDef("country", FieldType.STRING))
                .addField(new TextFieldDef("contact_details", FieldType.STRING).setArray(true))
                .addIndex(new IndexDef("idx_supplier_name", "supplier_name").setUnique(false))
                .addIndex(new IndexDef("idx_city", "city").setUnique(false))
        );

        schema.addEntity(new EntityDef("products")
                .addField(new NumericFieldDef("product_id", FieldType.LONG))
                .addField(new NumericFieldDef("supplier_id", FieldType.LONG).setPrimaryKeyPosition(11))
                .addField(new TextFieldDef("product_name", FieldType.STRING).setPrimaryKeyPosition(22))
                .addField(new TextFieldDef("color", FieldType.STRING))
                .addField(new NumericFieldDef("weight", FieldType.DOUBLE))
                .addField(new NumericFieldDef("price", FieldType.DOUBLE))
                .addField(new NumericFieldDef("discount", FieldType.DOUBLE))
                .addIndex(new IndexDef("idx_supplier_id_product_name", "supplier_id", "product_name"))
                .addIndex(new IndexDef("idx_price_discount", "price", "discount"))
                .addIndex(new IndexDef("idx_color", "color"))
        );

        schema.addEntityRelationship(new EntityRelationshipDef("fk_suppliers_products")
                .setDescription("this is fk_suppliers_products.")
                .setPrimaryEntityName("suppliers")
                .setForeignEntityName("products")
                .setCardinality(RelationshipCardinality.ONE_TO_MANY)
                .setForeignKeyFieldNames("supplier_id")
                .setOnUpdateAction(ForeignKeyAction.CASCADE)
                .setOnDeleteAction(ForeignKeyAction.RESTRICT));
        
        return schema;
        
    }

}
