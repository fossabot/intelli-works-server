package info.skyblond.velvet.scarlatina.models.note;

import java.io.Serializable;

public class Note implements Serializable {
    private int id;
    private String sno, studentName, teacherName, category, level, note, comment;
    private long timestamp;
    
    public Note(int id, String sno, String studentName, String teacherName, String category, String level, String note, String comment, long timestamp) {
        this.id = id;
        this.sno = sno;
        this.studentName = studentName;
        this.teacherName = teacherName;
        this.category = category;
        this.level = level;
        this.note = note;
        this.comment = comment;
        this.timestamp = timestamp;
    }
    
    public int getId() {
        return id;
    }
    
    public String getSno() {
        return sno;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getLevel() {
        return level;
    }
    
    public String getNote() {
        return note;
    }
    
    public String getComment() {
        return comment;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
