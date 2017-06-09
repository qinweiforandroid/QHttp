package com.qw.http.core;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by qinwei on 2017/6/9.
 */

public class Response implements Serializable {
    public int code;
    public InputStream inputStream;
    public HashMap<String, String> headers;
    public String message;

    /**
     * Returns the HTTP status code.
     */
    public int code() {
        return code;
    }

    /**
     * Returns true if the code is in [200..300), which means the request was successfully received,
     * understood, and accepted.
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

}
