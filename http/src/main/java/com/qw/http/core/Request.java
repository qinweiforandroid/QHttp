package com.qw.http.core;

import com.qw.http.exception.HttpException;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by qinwei on 2017/6/8.
 */

public class Request implements Serializable {
    public String url;
    public String postContent;
    public String tag;
    public RequestMethod method;
    public HashMap<String, String> headers;
    public HashMap<String, String> parameters;
    public boolean isCancelled;//是否取消
    public int delayTime;//延迟执行时间
    public int connect_timeout;
    public int read_timeout;


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

    /**
     * 检测请求是否被取消
     *
     * @throws HttpException
     */
    public void checkIfCancelled() throws HttpException {
        if (isCancelled) {
            throw new HttpException(HttpException.ErrorType.CANCEL, "the request has been cancelled");
        }
    }
}
