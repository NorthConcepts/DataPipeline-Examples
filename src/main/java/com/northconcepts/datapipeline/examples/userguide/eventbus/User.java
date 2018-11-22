package com.northconcepts.datapipeline.examples.userguide.eventbus;

public class User {

    private final String email;

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [email=" + email + "]";
    }

}
