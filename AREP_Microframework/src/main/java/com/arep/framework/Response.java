package com.arep.framework;

public class Response {
    private String body;
    private String type = "text/plain";

    public void setType(String type) { this.type = type; }
    public String getType() { return type; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
