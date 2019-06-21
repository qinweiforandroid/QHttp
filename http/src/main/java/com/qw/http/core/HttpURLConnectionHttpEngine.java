package com.qw.http.core;

import android.text.TextUtils;
import android.webkit.URLUtil;

import com.qw.http.RequestManager;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;
import com.qw.http.utils.HttpStringUtil;
import com.qw.http.utils.UploadUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by qinwei on 2017/6/9.
 */

public class HttpURLConnectionHttpEngine extends HttpEngine {
    private HttpURLConnection connection;



    @Override
    public Response get(Request request) throws HttpException {
        try {
            request.checkIfCancelled();
            HttpURLConnection connection = getConnection(request);
            request.checkIfCancelled();
            return buildResponse(connection);
        } catch (MalformedURLException e) {
            throw new HttpException(HttpException.ErrorType.UNKNOW, "url can't parse url=" + request.url);
        } catch (InterruptedIOException e) {
            throw new HttpException(HttpException.ErrorType.TIMEOUT, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    private void log(Request request) {
        HttpLog.d("url:" + request.url);
        HttpLog.d("method:" + request.method.name());
        HttpLog.d("headers:" + HttpStringUtil.buildParameterContent(request.headers));
        HttpLog.d("parameters:" + HttpStringUtil.buildParameterContent(request.parameters));
        HttpLog.d("postContent:" + request.postContent);
    }

    private Response buildResponse(HttpURLConnection connection) throws IOException, HttpException {
        Response response = new Response();
        response.code = connection.getResponseCode();
        response.addHeader("content-length", connection.getContentLength());
        response.addHeader("content-encoding", connection.getContentEncoding());
        response.addHeader("content-type", connection.getContentType());
        if (response.isSuccessful()) {
            String contentEncoding = connection.getContentEncoding();
            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                response.inputStream = new GZIPInputStream(connection.getInputStream());
            } else if (contentEncoding != null && contentEncoding.equalsIgnoreCase("deflate")) {
                response.inputStream = new InflaterInputStream(connection.getInputStream());
            } else {
                response.inputStream = connection.getInputStream();
            }
        } else {
            response.message = connection.getResponseMessage();
            throw new HttpException(HttpException.ErrorType.SERVER, response.message);
        }
        return response;
    }

    @Override
    public Response post(Request request, OnProgressUpdateListener listener) throws HttpException {
        try {
            request.checkIfCancelled();
            HttpURLConnection connection = getConnection(request);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            write(request, outputStream, listener);
            Response response = new Response();
            response.code = connection.getResponseCode();
            request.checkIfCancelled();
            return buildResponse(connection);
        } catch (MalformedURLException e) {
            throw new HttpException(HttpException.ErrorType.UNKNOW, "url can't parse url=" + request.url);
        } catch (InterruptedIOException e) {
            throw new HttpException(HttpException.ErrorType.TIMEOUT, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    @Override
    protected void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private HttpURLConnection getConnection(Request request) throws IOException, HttpException {
        String completedUrl = HttpStringUtil.buildCompletedUrl(request.url, request.parameters);
        URL url = new URL(completedUrl);
        log(request);
        if (URLUtil.isHttpsUrl(completedUrl)) {
            connection = (HttpsURLConnection) url.openConnection();
        } else if (URLUtil.isHttpUrl(completedUrl)) {
            connection = (HttpURLConnection) url.openConnection();
        } else {
            throw new HttpException(HttpException.ErrorType.UNKNOW, "the url :" + request.url + " is not valid");
        }
        connection.setConnectTimeout(request.connect_timeout);
        connection.setReadTimeout(request.read_timeout);
        connection.setRequestMethod(request.method.name());
        addHeaders(connection, request.headers);
        return connection;
    }


    @Override
    public void write(Request request, OutputStream outputStream, OnProgressUpdateListener listener) throws HttpException {
        try {
            if (request.uploadFile != null) {
                UploadUtil.uploadFile(outputStream, request.uploadFile);
            } else if (!TextUtils.isEmpty(request.postContent) && request.uploadFiles != null) {
                String encrypt = RequestManager.getInstance().getConfig().getSafeInterface().encrypt(request.postContent);
                HttpLog.d("加密后数据:" + encrypt);
                UploadUtil.upload(outputStream, encrypt, request.uploadFiles, listener);
                outputStream.write(encrypt.getBytes());
            } else if (!TextUtils.isEmpty(request.postContent)) {
                String encrypt = RequestManager.getInstance().getConfig().getSafeInterface().encrypt(request.postContent);
                HttpLog.d("加密后数据:" + encrypt);
                outputStream.write(encrypt.getBytes());
            } else if (request.parameters != null && request.parameters.size() > 0) {
                outputStream.write(HttpStringUtil.buildParameterContent(request.parameters).getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    /**
     * 设置http请求header
     *
     * @param connection
     * @param headers
     */
    private void addHeaders(HttpURLConnection connection, HashMap<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}