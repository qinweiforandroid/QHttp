package com.qw.http.core;

import com.qw.http.callback.OnProgressUpdateListener;
import com.qw.http.exception.HttpException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by qinwei on 2017/6/9.
 */

public abstract class HttpEngine {
    protected Request request;
    protected OnProgressUpdateListener listener;

    public Response execute() throws HttpException {
        return null;
    }


    protected abstract Response get() throws HttpException;

    protected abstract Response post() throws HttpException;

    protected abstract void write(OutputStream outputStream) throws IOException;

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        this.listener = listener;
    }
}
