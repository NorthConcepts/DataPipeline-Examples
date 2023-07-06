package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.FileInputStream;
import java.io.FileWriter;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class SaveAndLoadDataMappingToXML {

    public static void main(String[] args) throws Throwable {

        // Save DataMapping to XML file.
        DataMapping mapping = new DataMapping()
                .setName("Customer Details")
                .addFieldMapping(new FieldMapping("owner", "${source.customerid}").setCondition("${source.customerid} == 10001"))
                .addFieldMapping(new FieldMapping("first_name", "${source.customer_fname}"))
                .addFieldMapping(new FieldMapping("last_name", "${source.customer_lname}"))
                .addFieldMapping(new FieldMapping("name", "${target.first_name} + ' ' + ${target.last_name}"))
                .addFieldMapping(new FieldMapping("phone", "${source.customer-contact}"))
                .addFieldMapping(new FieldMapping("email", "${source.customer-email}"))
                .addFieldMapping(new FieldMapping("isActive", "${source.customer-active}"));

        EntityDef entityDef = new EntityDef()
                .setName("Order Details")
                .addField(new TextFieldDef("OrderName", FieldType.STRING)
                            .setRequired(true)
                            .setMinimumLength(0)
                            .setMaximumLength(2))
                .addField(new BooleanFieldDef("isActiveOrder", FieldType.BOOLEAN));

        mapping.setTargetValidationEntity(entityDef);

        mapping.toXml(new FileWriter("example/data/output/customer-details-data-mapping.xml"), true);
        
        //Load DataMapping from this XML File.
        DataMapping mappingFromXml = new DataMapping()
                .fromXml(new FileInputStream("example/data/output/customer-details-data-mapping.xml"));
        
        System.out.println("DataMapping loaded from XML:-\n" + mappingFromXml);
    }

}
