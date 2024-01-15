package com.northconcepts.datapipeline.examples.cookbook;

import java.util.HashSet;

import com.northconcepts.datapipeline.foundations.tools.GenerateRecordSerializers;

public class GenerateRecordSerializersExample {
    public static void main(String[] args) throws Throwable {
        HashSet<String> types = new HashSet<>();
        types.add(SimpleOrganization.class.getName());
        types.add(SimpleUser.class.getName());

        GenerateRecordSerializers serializers = new GenerateRecordSerializers();
        serializers.generate(types);
    }

}

class SimpleUser {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
}

class SimpleOrganization {
    private int id;
    private String name;
    private SimpleUser manager;
}
