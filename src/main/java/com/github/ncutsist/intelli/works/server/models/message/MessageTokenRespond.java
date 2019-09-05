package com.github.ncutsist.intelli.works.server.models.message;

import java.io.Serializable;
import java.util.Objects;

public class MessageTokenRespond implements Serializable {
    private boolean validate;
    private String name;
    private String ticket;
    
    private MessageTokenRespond(){}
    
    public static MessageTokenRespond getSuccess(String name, String ticket){
        Objects.requireNonNull(name, ticket);
        MessageTokenRespond respond = new MessageTokenRespond();
        respond.validate = true;
        respond.name = name;
        respond.ticket = ticket;
        return respond;
    }
    
    public static MessageTokenRespond getFailed(){
        MessageTokenRespond respond = new MessageTokenRespond();
        respond.validate = false;
        respond.name = "";
        respond.ticket = "";
        return respond;
    }
}
