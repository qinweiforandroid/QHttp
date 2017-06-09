package com.qw.http.core;

import java.util.HashMap;

/**
 * Created by qinwei on 2017/6/8.
 */

public class Request {
    public String url;
    public String postContent;
    public String tag;
    public RequestMethod method;
    public HashMap<String, String> headers;
    public HashMap<String, String> parameters;
    public boolean global = true;//是否全局异常处理

    public Request(String url) {
        this.url = url;
        this.method = RequestMethod.GET;
    }

    public Request(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
    }

    /**
     * 添加http头部信息
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(key, value);
    }
}
