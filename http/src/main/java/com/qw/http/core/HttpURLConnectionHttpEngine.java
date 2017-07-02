package com.qw.http.core;

import android.text.TextUtils;

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
        switch (request.method) {
            case GET:
            case PUT:
                return get();
            case POST:
            case DELETE:
                return post();
            default:
                throw new HttpException(HttpException.ErrorType.UNKNOW, "not support method[" + request.method.name() + "]");
        }
    }

    @Override
    public Response get() throws HttpException {
        try {
            HttpURLConnection connection = null;
            HttpLog.d("url:" + request.url);
            HttpLog.d("method:" + request.method.name());
            HttpLog.d("headers:" + HttpStringUtil.buildParameterContent(request.headers));
            HttpLog.d("parameters:" + HttpStringUtil.buildParameterContent(request.parameters));
            URL url = new URL(request.url);
            if (request.url.startsWith("https")) {
                connection = (HttpsURLConnection) url.openConnection();
                // FIXME: 2017/6/8 设置证书相关
            } else if (request.url.startsWith("http")) {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod(request.method.name());
            addHeaders(connection, request.headers);
            Response response = new Response();
            response.code = connection.getResponseCode();
            if (response.isSuccessful()) {
//            fixme set response headers
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
        } catch (MalformedURLException e) {
            throw new HttpException(HttpException.ErrorType.UNKNOW, "url can't parse url=" + request.url);
        } catch (InterruptedIOException e) {
            throw new HttpException(HttpException.ErrorType.TIMEOUT, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    @Override
    public Response post() throws HttpException {
        try {
            HttpURLConnection connection = null;
            URL url = new URL(request.url);
            if (request.url.startsWith("https")) {
                connection = (HttpsURLConnection) url.openConnection();
                // FIXME: 2017/6/8 设置证书相关
            } else if (request.url.startsWith("http")) {
                connection = (HttpURLConnection) url.openConnection();
            }
            addHeaders(connection, request.headers);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod(request.method.name());
            Response response = new Response();
            response.code = connection.getResponseCode();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            write(connection.getOutputStream());
            if (response.isSuccessful()) {
//            fixme set response headers
//            connection.getHeaderFields();
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
        } catch (MalformedURLException e) {
            throw new HttpException(HttpException.ErrorType.UNKNOW, "url can't parse url=" + request.url);
        } catch (InterruptedIOException e) {
            throw new HttpException(HttpException.ErrorType.TIMEOUT, e.getMessage());
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        if (!TextUtils.isEmpty(request.postContent)) {
            outputStream.write(request.postContent.getBytes());
        } else if (request.parameters != null && request.parameters.size() > 0) {
            outputStream.write(HttpStringUtil.buildParameterContent(request.parameters).getBytes());
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
