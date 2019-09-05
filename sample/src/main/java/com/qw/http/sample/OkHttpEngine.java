package com.qw.http.sample;

import android.text.TextUtils;

import com.qw.http.RequestManager;
import com.qw.http.core.FileEntity;
import com.qw.http.core.HttpEngine;
import com.qw.http.core.OnProgressUpdateListener;
import com.qw.http.core.Request;
import com.qw.http.core.Response;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by qinwei on 2019-06-15 17:28
 * email: qinwei_it@163.com
 */
public class OkHttpEngine extends HttpEngine {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private okhttp3.Response okResponse;

    @Override
    protected Response get(Request request) throws HttpException {
        try {
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                    .url(request.url);
            addHeaders(builder, request.headers);
            okResponse = client.newCall(builder.build()).execute();
            Response response = new Response();
            response.code = okResponse.code();
            response.inputStream = okResponse.body().byteStream();
            return response;
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    @Override
    protected Response post(Request request, OnProgressUpdateListener listener) throws HttpException {
        try {
            OkHttpClient client = new OkHttpClient();
            okResponse = client.newCall(builderOkRequest(request)).execute();
            Response response = new Response();
            response.code = okResponse.code();
            response.inputStream = okResponse.body().byteStream();
            return response;
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    private okhttp3.Request builderOkRequest(Request request) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(request.url);
        addHeaders(builder, request.headers);
        if (request.uploadFile != null) {
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            mMultipartBodyBuilder.setType(MultipartBody.FORM);
            mMultipartBodyBuilder.addFormDataPart("file",
                    request.uploadFile.substring(request.uploadFile.lastIndexOf("/") + 1),
                    RequestBody.create(MediaType.parse("application/octet-stream"), new File(request.uploadFile)));
            builder.post(mMultipartBodyBuilder.build());
        } else if (!TextUtils.isEmpty(request.postContent) && request.uploadFiles != null) {
            String encrypt = RequestManager.getInstance().getConfig().getSafeInterface().encrypt(request.postContent);
            HttpLog.d("加密后数据:" + encrypt);

            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            mMultipartBodyBuilder.setType(MultipartBody.FORM);
            for (FileEntity file : request.uploadFiles) {
                mMultipartBodyBuilder.addFormDataPart("file", file.getFileName(),
                        RequestBody.create(MediaType.parse(file.getFileType()),
                                new File(file.getFilePath())));
            }
            mMultipartBodyBuilder.addFormDataPart("data", "data", RequestBody.create(JSON, encrypt));
            builder.post(mMultipartBodyBuilder.build());
        } else if (!TextUtils.isEmpty(request.postContent)) {
            String encrypt = RequestManager.getInstance().getConfig().getSafeInterface().encrypt(request.postContent);
            HttpLog.d("加密后数据:" + encrypt);
            RequestBody body = RequestBody.create(JSON, encrypt);
            builder.post(body);
        } else if (request.parameters != null && request.parameters.size() > 0) {
            MultipartBody.Builder mMultipartBodyBuilder = new MultipartBody.Builder();
            mMultipartBodyBuilder.setType(MultipartBody.FORM);
            for (Map.Entry<String, String> entry : request.parameters.entrySet()) {
                mMultipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                builder.post(mMultipartBodyBuilder.build());
            }
        }
        return builder.build();
    }

    @Override
    protected void close() {
        if (okResponse != null) {
            okResponse.close();
        }
    }

    @Override
    protected void write(Request request, OutputStream outputStream, OnProgressUpdateListener listener) throws HttpException {

    }

    private void addHeaders(okhttp3.Request.Builder builder, HashMap<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
