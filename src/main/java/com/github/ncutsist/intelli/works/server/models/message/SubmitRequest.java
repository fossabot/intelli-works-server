package com.github.ncutsist.intelli.works.server.models.message;

import java.io.Serializable;
import java.util.Objects;

public class SubmitRequest implements Serializable {
    private long id;
    private String name;
    private String ticket;
    private String level;
    private String message;
    
    private SubmitRequest(){}
    
    public long getId() {
        return id;
    }
    
    public String getTicket() {
        return ticket;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLevel() {
        return level;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public boolean equals(Object o) {
        Objects.requireNonNull(ticket);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmitRequest that = (SubmitRequest) o;
        return ticket.equals(that.ticket);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ticket);
    }
    
    @Override
    public String toString() {
        return "SubmitRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ticket='" + ticket + '\'' +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
