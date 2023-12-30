package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.foundations.tools.GenerateRecordSerializers;

import java.util.HashSet;

public class GenerateRecordSerializersExample {
    public static void main(String[] args) throws Throwable {
        HashSet<String> types = new HashSet<>();
        types.add(SimpleOrganization.class.getName());
        types.add(SimpleUser.class.getName());

        GenerateRecordSerializers serializers = new GenerateRecordSerializers();
        serializers.generate(types);
    }

    public static class SimpleUser {
        private int id;
        private String name;
        private String address;
        private String phoneNumber;
    }

    public static class SimpleOrganization {
        private int id;
        private String name;
        private SimpleUser manager;
    }

}
