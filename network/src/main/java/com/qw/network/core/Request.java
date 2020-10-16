package com.qw.network.core;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by qinwei on 2020/7/10 2:36 PM
 * email: qinwei_it@163.com
 */
public class Request {

    public enum Method {
        /**
         * get请求
         */
        GET,
        /**
         * post请求
         */
        POST,
        DELETE,
        PUT
    }

    private String url;
    private String tag;
    private Method method;
    public String content;
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    public boolean sync;
    /**
     * 延迟执行时间
     */
    public int delayTime;
    /**
     * 连接超时时间
     */
    public int connect_timeout;
    /**
     * 读取超时时间
     */
    public int read_timeout;

    public Request(String url) {
        this.url = url;
        this.tag = this.toString();
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public HashMap<String, String> header(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return headers;
    }

    public HashMap<String, String> param(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return params;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getTag() {
        return tag;
    }

    public boolean hasUpload() {
        return content != null && content.length() > 0;
    }

}