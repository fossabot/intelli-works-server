package com.github.ncutsist.intelli.works.server.models.message;

import java.io.Serializable;

public class Message implements Serializable {
    private long msgId, id;
    private String name, message, level;
    private boolean status;
    private long timestamp;
    
    public Message(long msgId, long id, String name, String message, String level, boolean status, long timestamp) {
        this.msgId = msgId;
        this.id = id;
        this.name = name;
        this.message = message;
        this.level = level;
        this.status = status;
        this.timestamp = timestamp;
    }
    
    public long getMsgId() {
        return msgId;
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getLevel() {
        return level;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
