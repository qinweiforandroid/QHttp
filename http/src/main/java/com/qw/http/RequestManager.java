package com.qw.http;

import com.qw.http.core.ICallback;
import com.qw.http.core.OnGlobalExceptionListener;
import com.qw.http.core.Request;
import com.qw.http.core.RequestTask;
import com.qw.http.log.HttpLog;

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
    private HashMap<String, RequestTask> cache;

    private RequestManager() {
        mExecutors = Executors.newFixedThreadPool(10);
        cache = new HashMap<>();
    }

    public static RequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    public void execute(Request request, ICallback callback, OnGlobalExceptionListener listener) {
        if (!cache.containsKey(request.tag)) {
            RequestTask task = new RequestTask(request);
            task.setCallback(callback);
            task.setOnRequestTaskListener(this);
            task.setOnGlobalExceptionListener(listener);
            cache.put(request.tag, task);
            mExecutors.execute(task);
            return;
        }
    }

    public void execute(Request request, ICallback callback) {
        execute(request, callback, null);
    }

    public void cancel(String tag) {
        if (cache.containsKey(tag)) {
            cache.remove(tag).cancel();
        }
    }

    public void cancelAll() {
        if (cache.size() > 0) {
            for (Map.Entry<String, RequestTask> entry : cache.entrySet()) {
                entry.getValue().cancel();
            }
            cache.clear();
        }
    }

    @Override
    public void onPreExecute(String tag) {
        HttpLog.d("[" + tag + "] start ...");
    }

    @Override
    public void onExecuteCompleted(String tag) {
        HttpLog.d("tag[" + tag + "] execute completed will remove  from cache task size=" + cache.size());
        cache.remove(tag);
        HttpLog.d("tag[" + tag + "] execute completed      removed from cache task size=" + cache.size());
    }
}
