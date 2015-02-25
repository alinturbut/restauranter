package com.alinturbut.restauranter.model;

/**
 * @author alinturbut.
 */
public enum HttpRequestMethod {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), OPTIONS("OPTIONS");

    private String key;

    private HttpRequestMethod(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.key;
    }
}
