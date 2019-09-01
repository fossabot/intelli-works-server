package info.skyblond.velvet.scarlatina.models.message;

import java.io.Serializable;

public class Teacher implements Serializable {
    private String uid, name, key, token;
    private int counter;
    
    public Teacher(String uid, String name, String key, String token, int counter) {
        this.uid = uid;
        this.name = name;
        this.key = key;
        this.token = token;
        this.counter = counter;
    }
    
    public String getUid() {
        return uid;
    }
    
    public String getName() {
        return name;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getToken() {
        return token;
    }
    
    public int getCounter() {
        return counter;
    }
    
    @Override
    public String toString() {
        return "Teacher{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", token='" + token + '\'' +
                ", counter=" + counter +
                '}';
    }
}
