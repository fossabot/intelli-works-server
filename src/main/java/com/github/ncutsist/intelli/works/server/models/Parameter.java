package com.github.ncutsist.intelli.works.server.models;

import java.io.Serializable;

public class Parameter implements Serializable {
    private String fetchStudentBySno, fetchRecordWithToken, submitMessage, verifySubmitUrlToken, fetchSearchFormParameter;
    private String doSearch, fetchMessageWithToken, updateMessageWithId, verifyTeacherCode, verifyTeacherToken;
    private String createNotes, fetchNotesBySnoWithToken, fetchNotesByToken, fetchStudentFace;
    
    public String getFetchStudentBySno() {
        return fetchStudentBySno;
    }
    
    public String getFetchRecordWithToken() {
        return fetchRecordWithToken;
    }
    
    public String getSubmitMessage() {
        return submitMessage;
    }
    
    public String getVerifySubmitUrlToken() {
        return verifySubmitUrlToken;
    }
    
    public String getFetchSearchFormParameter() {
        return fetchSearchFormParameter;
    }
    
    public String getDoSearch() {
        return doSearch;
    }
    
    public String getFetchMessageWithToken() {
        return fetchMessageWithToken;
    }
    
    public String getUpdateMessageWithId() {
        return updateMessageWithId;
    }
    
    public String getVerifyTeacherCode() {
        return verifyTeacherCode;
    }
    
    public String getVerifyTeacherToken() {
        return verifyTeacherToken;
    }
    
    public String getCreateNotes() {
        return createNotes;
    }
    
    public String getFetchNotesBySnoWithToken() {
        return fetchNotesBySnoWithToken;
    }
    
    public String getFetchStudentFace() {
        return fetchStudentFace;
    }
    
    public String getFetchNotesByToken() {
        return fetchNotesByToken;
    }
}