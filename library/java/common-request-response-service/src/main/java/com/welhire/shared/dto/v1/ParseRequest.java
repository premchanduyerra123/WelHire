package com.welhire.shared.dto.v1;

public class ParseRequest {
    public String jdContentId;
    public String fileName;
    public ParseRequest() {}
    public ParseRequest(String jdContentId, String fileName) {
        this.jdContentId = jdContentId;
        this.fileName = fileName;
    }
}