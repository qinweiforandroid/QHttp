package com.qw.http.core;

import com.qw.http.exception.HttpException;

import java.io.OutputStream;

/**
 * @author qinwei
 * @date 2017/6/9
 */

public abstract class HttpEngine {
    public final Response execute(Request request, OnProgressUpdateListener listener) throws HttpException {
        switch (request.method) {
            case GET:
            case DELETE:
                return get(request);
            case POST:
            case PUT:
                return post(request, listener);
            default:
                throw new HttpException(HttpException.ErrorType.UNKNOW, "not support method[" + request.method.name() + "]");
        }
    }

    protected abstract Response get(Request request) throws HttpException;

    protected abstract Response post(Request request, OnProgressUpdateListener listener) throws HttpException;

    /**
     * 关闭请求引擎
     */
    protected abstract void close();

    /**
     * 写数据到服务器
     *
     * @param request
     * @param outputStream 输出流
     * @param listener
     * @throws HttpException
     */
    protected abstract void write(Request request, OutputStream outputStream, OnProgressUpdateListener listener) throws HttpException;
}
