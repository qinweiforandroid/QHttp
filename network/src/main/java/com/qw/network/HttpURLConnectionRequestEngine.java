package com.qw.network;

import com.qw.network.core.ICallback;
import com.qw.network.core.IRequestEngine;
import com.qw.network.core.Request;

/**
 * use HttpURLConnection do request
 * Created by qinwei on 2020/7/10 2:44 PM
 * email: qinwei_it@163.com
 *
 * @author qinwei
 */
public class HttpURLConnectionRequestEngine implements IRequestEngine {
    private HttpClient httpClient;

    public HttpURLConnectionRequestEngine() {
        httpClient = new HttpClient();
    }

    @Override
    public <T> void get(Request request, Config config, ICallback<T> callback) {
        request.setMethod(Request.Method.GET);
        httpClient.get(request, config, callback);
    }

    @Override
    public void cancel(String tag) {
        httpClient.cancel(tag);
    }

    @Override
    public void cancelAll() {

    }
}