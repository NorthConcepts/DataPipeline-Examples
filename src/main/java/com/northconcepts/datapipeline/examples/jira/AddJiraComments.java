package com.northconcepts.datapipeline.examples.jira;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jira.client.JiraService;

public class AddJiraComments {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";

    private static final String JIRA_ISSUE_ID = "ISSUE_ID";
    
    public static void main(String... args) {
        JiraService service = new JiraService(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY);
        
        Record record = service.addIssueComment(JIRA_ISSUE_ID, "What is the progress on this issue?");
        System.out.println(record);
    }
}
