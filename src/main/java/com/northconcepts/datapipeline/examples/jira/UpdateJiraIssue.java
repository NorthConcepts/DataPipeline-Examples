package com.northconcepts.datapipeline.examples.jira;

import static org.junit.Assert.assertNotNull;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jira.JiraIssue;
import com.northconcepts.datapipeline.jira.client.JiraService;

public class UpdateJiraIssue {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";
    
    public static void main(String[] args) {
        JiraService service = new JiraService(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY);
        
        String key = createJiraIssueAndGetIssueKey(service);
        
        JiraIssue jiraIssue = new JiraIssue()
                .setSummary("Updated Summary - Create A Jira Issue")
                .setDescription("Updated Description - This is a story to understand how to create jira issue.");
                
        service.updateIssue(key, jiraIssue);
    }

    private static String createJiraIssueAndGetIssueKey(JiraService service) {
        JiraIssue jiraIssue = new JiraIssue()
            .setProject("AB")
            .setIssueType(10001)
            .setSummary("Create A Jira Issue")
            .setDescription("This is a story to understand how to create jira issue.")
            ;

        System.out.println(jiraIssue);

        Record response = service.createIssue(jiraIssue);
        assertNotNull(response.getFieldValueAsString("id", null));
        
        String key = response.getFieldValueAsString("key", null);
        assertNotNull(key);
        System.out.println(response);
        
        return key;
    }
}
