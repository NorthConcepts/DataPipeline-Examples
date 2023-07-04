package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.jdbc.sql.select.Select;

public class GenerateSqlQueriesProgrammatically {

    public static void main(String[] args) {
        Select select = new Select("user")
                .select("*")
                .where("id = ?", 12)
                .orderBy("firstName");
        
        String query = select.getSqlFragment();
        System.out.println("Select query: " + query);
        
    }
}
