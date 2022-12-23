package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;

public class ShowDataMappingProblems {

    public static void main(String[] args) {
        DataMapping mapping = createDataMapping();
        
        System.out.println("----------------Direct DataMapping Problems------------------");
        System.out.println(mapping.getDataMappingProblems());
        System.out.println("----------------All DataMapping Problems------------------");
        System.out.println(mapping.getDataMappingProblems(true));
        System.out.println("------------------");
    }

    public static DataMapping createDataMapping() {
        
                return new DataMapping()
                        .setName("")
                        .addFieldMapping(new FieldMapping("order", "${source.orderid}"))
                        .addFieldMapping(new FieldMapping("owner", "${source.customerid}"))
                        .addCondition("length(customerid) > 3")
                        .addFieldMapping(new FieldMapping("first_name", "${source.customer_fname}")
                                .setCondition("length(customer_fname) > 3"))
                        .addFieldMapping(new FieldMapping("last_name", "${source.customer_lname}"))
                        .addFieldMapping(new FieldMapping("last_name", "${source.customer_lname}"))
                        .addFieldMapping(new FieldMapping("name", "${target.first_name} + ' ' + ${source.customer_initial} + '. ' +${target.last_name}"))
                        .addFieldMapping(new FieldMapping("phone", "${source.customer-contact}"))
                        .addFieldMapping(new FieldMapping("email", "${source.customer-email}"))
                        .addFieldMapping(new FieldMapping("isActive", "${source.customer-active}"));
    }
}
