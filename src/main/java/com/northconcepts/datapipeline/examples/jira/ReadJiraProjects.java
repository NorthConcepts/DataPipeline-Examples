package com.northconcepts.datapipeline.examples.jira;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.jira.JiraProjectReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadJiraProjects {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";

    public static void main(String... args) {
        JiraProjectReader reader = new JiraProjectReader(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY);
        reader.setMaxItems(2);
        reader.setOrderByField(JiraProjectReader.OrderByField.KEY);

        Job.run(reader, new StreamWriter(System.out));
    }
}
