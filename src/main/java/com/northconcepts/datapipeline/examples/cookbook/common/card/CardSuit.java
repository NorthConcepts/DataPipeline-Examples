/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.common.card;

public enum CardSuit {
    
    DIAMONDS, CLUBS, HEARTS, SPADES;

    public int getValue() {
        return ordinal() + 1;
    }

}
