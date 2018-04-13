package com.qw.http;

import android.text.TextUtils;

import com.qw.http.core.ICallback;
import com.qw.http.core.OnGlobalExceptionListener;
import com.qw.http.core.Request;
import com.qw.http.core.RequestConfig;
import com.qw.http.core.RequestTask;
import com.qw.http.log.HttpLog;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by qinwei on 2017/6/8.
 */

public class RequestManager implements RequestTask.OnRequestTaskListener {
    private static RequestManager mInstance;
    private ExecutorService mExecutors;
    private HashMap<String, RequestTask> mRequestTaskCache;

    private RequestConfig mRequestConfig;

    private RequestManager() {
        mExecutors = Executors.newFixedThreadPool(10);
        mRequestTaskCache = new HashMap<>();
        this.mRequestConfig = RequestConfig.getDefault();
    }

    public void config(RequestConfig config) {
        this.mRequestConfig = config;
    }

    public static RequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    /**
     * 开子线程执行请求任务
     *
     * @param request  请求信息
     * @param callback 请求过程中回调函数
     * @param listener 全局异常处理
     */
    public void execute(Request request, ICallback callback, OnGlobalExceptionListener listener) {
        if (!mRequestTaskCache.containsKey(request.tag)) {
            RequestTask task = buildRequestTask(request, callback, listener);
            task.setOnRequestTaskListener(this);
            mRequestTaskCache.put(request.tag, task);
            mExecutors.execute(task);
        }
    }

    /**
     * 在当前线程中执行请求任务
     *
     * @param request  请求信息
     * @param callback 请求过程中回调函数
     */
    public void executeInMainThread(Request request, ICallback callback) {
        RequestTask task = buildRequestTask(request, callback, null);
        task.setOnRequestTaskListener(this);
        //直接在当前线程中执行
        task.startExecuteInCurrentThread();
    }

    private RequestTask buildRequestTask(Request request, ICallback callback, OnGlobalExceptionListener listener) {
        //设置连接超时时间
        if (request.connect_timeout == 0) {
            request.connect_timeout = mRequestConfig.connect_timeout;
        }
        //设置读取超时时间
        if (request.read_timeout == 0) {
            request.read_timeout = mRequestConfig.read_timeout;
        }
        if (TextUtils.isEmpty(request.tag)) {
            request.tag = new Date().toLocaleString();
        }
        RequestTask task = new RequestTask(request);
        task.setHttpEngine(mRequestConfig.httpEngine);
        task.setCallback(callback);
        task.setOnGlobalExceptionListener(listener);
        return task;
    }

    public void execute(Request request, ICallback callback) {
        execute(request, callback, null);
    }

    public void cancel(String tag) {
        if (mRequestTaskCache.containsKey(tag)) {
            mRequestTaskCache.remove(tag).cancel();
        }
    }

    public void cancelAll() {
        if (mRequestTaskCache.size() > 0) {
            for (Map.Entry<String, RequestTask> entry : mRequestTaskCache.entrySet()) {
                entry.getValue().cancel();
            }
            mRequestTaskCache.clear();
        }
    }

    @Override
    public synchronized void onPreExecute(String tag) {
        HttpLog.d("request tag[" + tag + "] start execute ...");
    }

    @Override
    public synchronized void onExecuteCompleted(String tag) {
        HttpLog.d("request tag[" + tag + "] execute completed will remove  from mRequestTaskCache task size=" + mRequestTaskCache.size());
        mRequestTaskCache.remove(tag);
        HttpLog.d("request tag[" + tag + "] execute completed      removed from mRequestTaskCache task size=" + mRequestTaskCache.size());
    }

    public RequestConfig getConfig() {
        return mRequestConfig;
    }
}
