package com.northconcepts.datapipeline.examples.cookbook.common.card;

public enum CardNumber {
    
    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;
    
    public int getValue() {
        return ordinal() + 1;
    }

}
