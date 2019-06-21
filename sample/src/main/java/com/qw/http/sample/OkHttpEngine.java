package com.qw.http.sample;

import com.qw.http.core.HttpEngine;
import com.qw.http.core.OnProgressUpdateListener;
import com.qw.http.core.Request;
import com.qw.http.core.Response;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;

import java.io.OutputStream;

/**
 * Created by qinwei on 2018/1/14.
 */

public class OkHttpEngine extends HttpEngine {
    @Override
    public Response execute() throws HttpException {
        HttpLog.d("execute" + request.url);
        return null;
    }

    @Override
    protected Response get() throws HttpException {
        HttpLog.d("get" + request.url);
        return null;
    }

    @Override
    protected Response post() throws HttpException {
        HttpLog.d("post");
        return null;
    }

    @Override
    protected void close() {

    }

    @Override
    protected void write(Request request, OutputStream outputStream, OnProgressUpdateListener listener) throws HttpException {
        HttpLog.d("write");
    }
}
