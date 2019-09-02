package com.github.ncutsist.intelli.works.server.models;

import java.io.Serializable;

public class ValidateResponse implements Serializable {
    private String teacherName;
    private int unread;
    
    public ValidateResponse(String teacherName, int unread) {
        this.teacherName = teacherName;
        this.unread = unread;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public int getUnread() {
        return unread;
    }
}
