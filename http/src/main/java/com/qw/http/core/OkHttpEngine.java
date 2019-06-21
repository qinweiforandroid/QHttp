package com.qw.http.core;

import com.qw.http.exception.HttpException;

import java.io.IOException;
import java.io.OutputStream;

import okhttp3.OkHttpClient;

/**
 * Created by qinwei on 2019-06-15 17:28
 * email: qinwei_it@163.com
 */
public class OkHttpEngine extends HttpEngine {


    @Override
    protected Response get(Request request) throws HttpException {
        try {
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request okRequest = new okhttp3.Request.Builder()
                    .url(request.url)
                    .build();
            okhttp3.Response okResponse = client.newCall(okRequest).execute();
            Response response = new Response();
            response.code = okResponse.code();
            response.inputStream = okResponse.body().byteStream();
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
        return null;
    }

    @Override
    protected Response post(Request request, OnProgressUpdateListener listener) throws HttpException {
        return null;
    }

    @Override
    protected void close() {

    }

    @Override
    protected void write(Request request, OutputStream outputStream, OnProgressUpdateListener listener) throws HttpException {

    }
}
