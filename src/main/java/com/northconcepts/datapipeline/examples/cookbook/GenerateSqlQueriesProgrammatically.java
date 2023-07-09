package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.jdbc.sql.SqlPart;
import com.northconcepts.datapipeline.jdbc.sql.select.Select;

public class GenerateSqlQueriesProgrammatically {

    public static void main(String[] args) {
        SqlPart select = new Select("user")
                .select("*")
                .where("id = ? AND role = ?", 12, "admin")
                .orderBy("firstName");
        
        String query = select.getSqlFragment();
        System.out.println("Select query: " + query);
        
        System.out.println("Query parameters: ");
        for (Object value : select.getParameterValues()) {
            System.out.println(value.toString());
        }
    }
}
