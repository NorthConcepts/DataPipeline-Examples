package com.northconcepts.datapipeline.examples.cookbook.common.card;

public enum CardSuit {
    
    DIAMONDS, CLUBS, HEARTS, SPADES;

    public int getValue() {
        return ordinal() + 1;
    }

}
