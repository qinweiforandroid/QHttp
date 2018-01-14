package com.qw.http.core;

import android.text.TextUtils;
import android.webkit.URLUtil;

import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;
import com.qw.http.utils.HttpStringUtil;

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

    @Override
    public Response execute() throws HttpException {
        if (request.delayTime > 0) {
            try {
                Thread.sleep(request.delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        switch (request.method) {
            case GET:
            case DELETE:
                return get();
            case POST:
            case PUT:
                return post();
            default:
                throw new HttpException(HttpException.ErrorType.UNKNOW, "not support method[" + request.method.name() + "]");
        }
    }

    @Override
    public Response get() throws HttpException {
        try {
            request.checkIfCancelled();
            String completedUrl = HttpStringUtil.buildCompletedUrl(request.url, request.parameters);
            HttpURLConnection connection = getConnection(completedUrl);
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

    private void log() {
        HttpLog.d("url:" + request.url);
        HttpLog.d("method:" + request.method.name());
        HttpLog.d("headers:" + HttpStringUtil.buildParameterContent(request.headers));
        HttpLog.d("parameters:" + HttpStringUtil.buildParameterContent(request.parameters));
        HttpLog.d("content:" + request.postContent);
    }

    private Response buildResponse(HttpURLConnection connection) throws IOException, HttpException {
        Response response = new Response();
        response.code = connection.getResponseCode();
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
    public Response post() throws HttpException {
        try {
            request.checkIfCancelled();
            HttpURLConnection connection = getConnection(request.url);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            write(outputStream);
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

    private HttpURLConnection getConnection(String completedUrl) throws IOException, HttpException {
        HttpURLConnection connection = null;
        URL url = new URL(completedUrl);
        log();
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
    public void write(OutputStream outputStream) throws IOException {
        if (!TextUtils.isEmpty(request.postContent)) {
            outputStream.write(request.postContent.getBytes());
        } else if (request.parameters != null && request.parameters.size() > 0) {
            outputStream.write(HttpStringUtil.buildParameterContent(request.parameters).getBytes());
        }
        outputStream.flush();
        outputStream.close();
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
