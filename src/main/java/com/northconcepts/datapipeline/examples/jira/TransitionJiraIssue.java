/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.jira;

import com.northconcepts.datapipeline.jira.client.JiraService;

public class TransitionJiraIssue {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";

    private static final String JIRA_ISSUE_ID = "ISSUE_ID";
    private static final int TRANSITION_ID = 123;
    
    public static void main(String... args) {
        JiraService service = new JiraService(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY);
        
        service.transitionIssue(JIRA_ISSUE_ID, TRANSITION_ID);
    }
}
