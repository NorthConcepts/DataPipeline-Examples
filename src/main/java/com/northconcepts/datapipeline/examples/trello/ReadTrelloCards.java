package com.northconcepts.datapipeline.examples.trello;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.trello.CardFilter;
import com.northconcepts.datapipeline.trello.TrelloBoardCardsReader;

public class ReadTrelloCards {

    private static final String TRELLO_API_KEY = "API_KEY";
    private static final String TRELLO_API_TOKEN = "API_TOKEN";
    private static final String TRELLO_BOARD_ID= "BOARD_ID";

    public static void main(String[] args) {
        DataReader reader = new TrelloBoardCardsReader(TRELLO_API_KEY, TRELLO_API_TOKEN, TRELLO_BOARD_ID)
                .setCardFilter(CardFilter.CLOSED)
                ;
        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
    
}
