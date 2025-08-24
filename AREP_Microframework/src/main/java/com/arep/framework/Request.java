package com.arep.framework;

import java.net.URI;
import java.util.*;

public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> queryParams;
    private final String body;

    public Request(String method, URI uri, String body){
        this.method = method;
        this.path = uri.getPath();
        this.body = body;

        this.queryParams = new HashMap<>();
        if(uri.getQuery() != null){
            for (String pair : uri.getQuery().split("&")) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) queryParams.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getBody() { return body; }
    public String getValues(String key) { return queryParams.getOrDefault(key, ""); }
}
