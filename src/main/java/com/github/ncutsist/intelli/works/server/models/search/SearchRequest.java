package com.github.ncutsist.intelli.works.server.models.search;

import java.io.Serializable;

public class SearchRequest implements Serializable {
    private String gender = "any", politic = "any", grade = "any", cls = "any", dorm = "any", nationHelp = "any", other = "";
    private String token;
    
    private SearchRequest() {}
    
    public String getGender() {
        return gender;
    }
    
    public String getPolitic() {
        return politic;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public String getCls() {
        return cls;
    }
    
    public String getDorm() {
        return dorm;
    }
    
    public String getNationHelp() {
        return nationHelp;
    }
    
    public String getOther() {
        return other;
    }
    
    public String getToken() {
        return token;
    }
    
    @Override
    public String toString() {
        return "SearchRequest{" +
                "gender='" + gender + '\'' +
                ", politic='" + politic + '\'' +
                ", grade='" + grade + '\'' +
                ", cls='" + cls + '\'' +
                ", dorm='" + dorm + '\'' +
                ", nationHelp='" + nationHelp + '\'' +
                ", other='" + other + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
