package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.sql.mysql.Insert;

public class GenerateMySqlInsertStatementsProgrammatically {


    public static void main(String[] args) {
        Insert insert = new Insert("users")
                .add("id", 1)
                .add("name", "'John Doe'")
                .add("dateOfBirth", "'1990-01-01'")
                .add("email", "'johndoe@gmail.com'")
                .add("password_hash", "'#ag3!axx123111@'")
                .nextRow()
                .add("id", 2)
                .add("name", "'Alice Bob'")
                .add("dateOfBirth", "'1991-02-03'")
                .add("email", "'alicebob@gmail.com'")
                .add("password_hash", "'#ag3!axx123111@'")
                .nextRow()
                .add("id", 3)
                .add("name", "'Charlie Brown'")
                .add("dateOfBirth", "'1992-03-04'")
                .add("email", "'charliebrown@gmail.com'")
                .add("password_hash", "'#ag3!axx123111@'")
                ;

        insert.setPretty(true); // pretty print the generated SQL

        System.out.println(insert.getSqlFragment());
    }
}
