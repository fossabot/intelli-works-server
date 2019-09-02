package com.github.ncutsist.intelli.works.server.models.message;

import java.io.Serializable;

public class Record implements Serializable {
    private String sno, name;
    private long timestamp;
    
    public Record(String sno, String name, long timestamp) {
        this.sno = sno;
        this.name = name;
        this.timestamp = timestamp;
    }
    
    public String getSno() {
        return sno;
    }
    
    public String getName() {
        return name;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
