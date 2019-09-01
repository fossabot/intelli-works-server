package info.skyblond.velvet.scarlatina.models.message;

import java.io.Serializable;
import java.util.Objects;

public class TeacherTokenRespond implements Serializable {
    private boolean validate;
    private String name;
    private String ticket;
    
    private TeacherTokenRespond(){}
    
    public static TeacherTokenRespond getSuccess(String name, String ticket){
        Objects.requireNonNull(name, ticket);
        TeacherTokenRespond respond = new TeacherTokenRespond();
        respond.validate = true;
        respond.name = name;
        respond.ticket = ticket;
        return respond;
    }
    
    public static TeacherTokenRespond getFailed(){
        TeacherTokenRespond respond = new TeacherTokenRespond();
        respond.validate = false;
        respond.name = "";
        respond.ticket = "";
        return respond;
    }
}
