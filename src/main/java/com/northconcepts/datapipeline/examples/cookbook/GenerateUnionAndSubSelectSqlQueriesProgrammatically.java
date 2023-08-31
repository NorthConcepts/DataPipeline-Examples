package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.jdbc.sql.SqlPart;
import com.northconcepts.datapipeline.jdbc.sql.select.Select;
import com.northconcepts.datapipeline.jdbc.sql.select.Union;

public class GenerateUnionAndSubSelectSqlQueriesProgrammatically {
    public static void main(String[] args) {
        SqlPart select = new Select(
                new Union(
                        new Select("employees").select("employee_id", "first_name"),
                        new Select("contractors").select("employee_id", "first_name")
                ).setNestedAlias("employee_details"))
                .where("first_name = ?", "John");

        System.out.println("Select query: " + select.getSqlFragment());

        System.out.println("Query parameters: ");
        for (Object value : select.getParameterValues()) {
            System.out.println(value.toString());
        }
    }
}
