/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.common.card;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;

public class CardReader extends DataReader {
    
    private static final long MINIMUM_THREAD_SLEEP_TIME = 10L;
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Long maxRecords;
    private Long maxRunTime;
    private Long closeTime;
//    private Long readDelay;
    private Long readPeriod;
    private long nextReadOn;
    private Date startingDate = new Date();
    private long dateIncrement = 100;
    private long dateOffset;
    private boolean blockAfterEof;
    private int eofCount;

    public CardReader() {
    }
    
    public Long getMaxRecords() {
        return maxRecords;
    }
    
    public CardReader setMaxRecords(long maxRecords) {
        this.maxRecords = maxRecords;
        return this;
    }
    
    public CardReader setMaxRecords(Long maxRecords) {
        this.maxRecords = maxRecords;
        return this;
    }
    
    public Long getMaxRunTime() {
        return maxRunTime;
    }
    
    public CardReader setMaxRunTime(long maxRunTime) {
        this.maxRunTime = maxRunTime;
        return this;
    }
    
    public CardReader setMaxRunTime(Long maxRunTime) {
        this.maxRunTime = maxRunTime;
        return this;
    }
    
    @Override
    public void open() throws DataException {
        super.open();
        if (maxRunTime != null) {
            closeTime = getOpenedOn() + maxRunTime;
        }
    }
    
/*   public Long getReadDelay() {
       return readDelay;
   }
   
   public CardReader setReadDelay(Long readDelay) {
        this.readDelay = readDelay;
       return this;
    }*/
    
    public Long getReadPeriod() {
        return readPeriod;
    }
    
    public CardReader setReadPeriod(long readPeriod) {
        this.readPeriod = readPeriod;
        return this;
    }
    
    public CardReader setReadPeriod(Long readPeriod) {
        this.readPeriod = readPeriod;
        return this;
    }
    
    public Date getStartingDate() {
        return startingDate;
    }
    
    public CardReader setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
        return this;
    }

    public CardReader setStartingDate(String startingDate) {
        try {
            this.startingDate = TIMESTAMP_FORMAT.parse(startingDate);
        } catch (Throwable e) {
            throw DataException.wrap(e).set("startingDate", startingDate);
        }
        return this;
    }
    
    public long getDateIncrement() {
        return dateIncrement;
    }
    
    public CardReader setDateIncrement(long dateIncrement) {
        this.dateIncrement = dateIncrement;
        return this;
    }
    
    public boolean isBlockAfterEof() {
        return blockAfterEof;
    }
    
    public CardReader setBlockAfterEof(boolean blockAfterEof) {
        this.blockAfterEof = blockAfterEof;
        return this;
    }

    @Override
    protected Record readImpl() throws Throwable {
        
        if (eofCount > 0 && blockAfterEof) {
            synchronized (this) {
                wait();
            }
        }
        
        
        final long id = getRecordCount();
        
        if (maxRecords != null && id >= maxRecords) {
            eofCount++;
            return null;
        }
        
        if (closeTime != null && System.currentTimeMillis() >= closeTime) {
            eofCount++;
            return null;
        }
        
//        if (readDelay != null) {
//            Thread.sleep(readDelay);
//        }
        
        Record record = new Record();
        record.setField("id", id);
        
        CardSuit suit = CardSuit.values()[Math.abs((int)(id / CardNumber.values().length % CardSuit.values().length))];
        record.setField("card_suit", suit);
        record.setField("card_suit_value", suit.getValue());
        
        CardNumber number = CardNumber.values()[Math.abs((int)(id % CardNumber.values().length))];
        record.setField("card_number", number);
        record.setField("card_number_value", number.getValue());
        
        record.setField("card_value", (suit.ordinal() * CardNumber.values().length) + number.ordinal());
        record.setField("card_value_100s", (suit.getValue() * 100) + number.getValue());
        
        record.setField("date", new Date(startingDate.getTime() + dateOffset));
        record.setField("date_offset", dateOffset);
        int[] testArray={1,2,3};
        record.setField("test_array",testArray);
        
        
        dateOffset += dateIncrement;

        if (readPeriod != null) {
            if (nextReadOn > 0) {
                long delay = nextReadOn - System.currentTimeMillis();
//                System.out.println(delay);
                if (delay >= MINIMUM_THREAD_SLEEP_TIME) {
                    Thread.sleep(delay);
                }
                nextReadOn += readPeriod;
            } else {
                nextReadOn = System.currentTimeMillis() + readPeriod;
            }
        }

       
        //record.setField("currentTimeMillis", System.currentTimeMillis());
        
        //throw new RuntimeException();
        return record;
    }

}
