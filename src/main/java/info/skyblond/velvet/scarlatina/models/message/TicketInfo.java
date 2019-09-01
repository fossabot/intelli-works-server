package info.skyblond.velvet.scarlatina.models.message;

import java.io.Serializable;

public class TicketInfo implements Serializable {
    private Teacher teacher;
    private long createEpoch;
    
    public TicketInfo(Teacher teacher) {
        this.teacher = teacher;
        this.createEpoch = System.currentTimeMillis() / 1000;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public boolean isExpired(){
        return (System.currentTimeMillis() / 1000 - createEpoch) > 10 * 60;
    }
}
