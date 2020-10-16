package com.qw.network;


import com.qw.network.core.ICallback;
import com.qw.network.core.Request;
import com.qw.network.log.HttpLog;
import com.qw.network.utils.URLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by qinwei on 2020/7/10 3:14 PM
 * email: qinwei_it@163.com
 */
public class HttpClient {
    private ExecutorService mExecutors;
    private HashMap<String, RequestTask> tasks;
//    private LinkedBlockingQueue<Request> queue;

    public HttpClient() {
        mExecutors = Executors.newCachedThreadPool();
        tasks = new HashMap<>();
    }

    public <T> void get(Request request, Config config, ICallback<T> callback) {
        RequestTask<T> task = new RequestTask<>(request);
        task.setRequestCallback(callback);
        task.setOnRequestTaskListener(new OnRequestTaskListener() {

            @Override
            public void onCompleted(String tag) {
                tasks.remove(tag);
            }
        });
        tasks.put(request.getTag(), task);
        if (request.sync) {
            task.execute();
        } else {
            mExecutors.execute(task);
        }
    }

    public void cancel(String tag) {
        if (tasks.containsKey(tag)) {
            RequestTask task = tasks.remove(tag);
            if (task != null) {
                task.cancel();
            }
        }
    }

    public static class RequestTask<T> implements Runnable {
        private Request request;
        private OnRequestTaskListener listener;
        private ICallback<T> callback;

        public RequestTask(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            execute();
        }

        public void setOnRequestTaskListener(OnRequestTaskListener listener) {
            this.listener = listener;
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

        public void execute() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(request.getUrl());
                if (URLUtil.isHttpsUrl(request.getUrl())) {
                    connection = (HttpsURLConnection) url.openConnection();
                } else if (URLUtil.isHttpUrl(request.getUrl())) {
                    connection = (HttpURLConnection) url.openConnection();
                } else {
                    throw new HttpException(HttpException.ErrorType.UNKNOW, "the url :" + request.getUrl() + " is not valid");
                }
                HttpLog.d("request url:" + request.getUrl());
                connection.setConnectTimeout(request.connect_timeout);
                connection.setReadTimeout(request.read_timeout);
                connection.setRequestMethod(request.getMethod().name());
                addHeaders(connection, request.getHeaders());
                connection.connect();
                if (request.hasUpload()) {
                    OutputStream os = connection.getOutputStream();
                    if (request.content != null && request.content.length() > 0) {
                        os.write(request.content.getBytes());
                    }
                }
                int code = connection.getResponseCode();
                HttpLog.d("responseCode:" + code);
                HttpLog.d("responseLength:" + connection.getContentLength());
                String contentEncoding = connection.getContentEncoding();
                InputStream is;
                if (code >= 200 && code < 300) {
                    if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                        is = new GZIPInputStream(connection.getInputStream());
                    } else if (contentEncoding != null && contentEncoding.equalsIgnoreCase("deflate")) {
                        is = new InflaterInputStream(connection.getInputStream());
                    } else {
                        is = connection.getInputStream();
                    }
                    callback.onSuccess(callback.parse(is));
                } else {
                    if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                        is = new GZIPInputStream(connection.getErrorStream());
                    } else if (contentEncoding != null && contentEncoding.equalsIgnoreCase("deflate")) {
                        is = new InflaterInputStream(connection.getErrorStream());
                    } else {
                        is = connection.getErrorStream();
                    }
                    HttpException e = new HttpException(HttpException.ErrorType.SERVER, connection.getResponseMessage());
                    e.setCode(code);
                    e.setData(callback.error(is));
                    callback.onFailure(e);
                }
            } catch (IOException e) {
                callback.onFailure(new HttpException(HttpException.ErrorType.IO, e.getMessage()));
            } catch (HttpException e) {
                callback.onFailure(e);
            } catch (Exception e) {
                callback.onFailure(new HttpException(HttpException.ErrorType.UNKNOW, e.getMessage()));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            listener.onCompleted(request.getTag());
        }

        public void cancel() {
            callback.cancel();
        }

        public void setRequestCallback(ICallback<T> callback) {
            this.callback = callback;
        }
    }

    interface OnRequestTaskListener {

        /**
         * 请求任务执行完成
         *
         * @param tag
         */
        void onCompleted(String tag);
    }
}