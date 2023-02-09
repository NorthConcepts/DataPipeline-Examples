/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.jira;

import static org.junit.Assert.assertNotNull;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jira.JiraIssue;
import com.northconcepts.datapipeline.jira.client.JiraService;

public class CreateJiraIssue {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";
    
    public static void main(String[] args) {
        JiraService service = new JiraService(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY);
        
        JiraIssue jiraIssue = new JiraIssue()
            .setProject("AB")
            .setIssueType(10001)
            .setSummary("Create A Jira Issue")
            .setDescription("This is a story to understand how to create jira issue.")
            //.addCustomField("custom_field_01", "CustomValue") // Set custom fields if any
            ;

        System.out.println(jiraIssue);

        Record response = service.createIssue(jiraIssue);
        assertNotNull(response.getFieldValueAsString("id", null));
        assertNotNull(response.getFieldValueAsString("key", null));
        System.out.println(response);
    }
}
