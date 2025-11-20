package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.CollectionReader;

import java.util.ArrayList;
import java.util.List;

public class ReadFromACollection {

    public static class User {
        private final int id;
        private final String username;
        private final String email;
        private final boolean isActive;

        public User(int id, String username, String email, boolean isActive) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.isActive = isActive;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public boolean isActive() {
            return isActive;
        }
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "john.doe", "john.doe@example.com", true));
        users.add(new User(2, "jane.smith", "jane.smith@example.com", false));
        users.add(new User(3, "admin", "admin@example.com", true));

        DataReader reader = new CollectionReader<>(users, user -> {
            Record record = new Record();
            record.setField("user_id", user.getId());
            record.setField("username", user.getUsername());
            record.setField("email_address", user.getEmail());
            record.setField("active", user.isActive());
            return record;
        });

        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);
    }
}