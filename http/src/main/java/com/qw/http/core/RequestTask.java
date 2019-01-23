package com.qw.http.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.qw.http.exception.HttpException;
import com.qw.http.utils.HttpConstants;

/**
 * Created by qinwei on 2017/6/8.
 */

public class RequestTask implements Runnable {
    private Request mRequest;
    private ICallback callback;
    private OnRequestTaskListener nRequestTaskListener;
    private HttpEngine httpEngine;
    private OnGlobalExceptionListener onGlobalExceptionListener;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HttpConstants.SUCCESS:
                    callback.onSuccess(msg.obj);
                    break;
                case HttpConstants.FAIL:
                    HttpException e = (HttpException) msg.obj;
                    e.printStackTrace();
                    if (onGlobalExceptionListener == null || onGlobalExceptionListener.onHandlerGlobalException(e)) {
                        callback.onFailure(e);
                    }
                    break;
                default:
                    break;
            }
            nRequestTaskListener.onExecuteCompleted(mRequest.tag);
        }
    };

    public RequestTask(Request mRequest) {
        this.mRequest = mRequest;
    }

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }


    @Override
    public void run() {
        nRequestTaskListener.onPreExecute(mRequest.tag);
        try {
            Object obj = callback.preRequest(mRequest);
            if (obj != null) {
                sendMessageToMainThread(HttpConstants.SUCCESS, obj);
                return;
            }
            if (httpEngine == null) {
                httpEngine = new HttpURLConnectionHttpEngine();
            }
            httpEngine.setRequest(mRequest);
            httpEngine.setOnProgressUpdateListener(new OnProgressUpdateListener() {
                @Override
                public void onProgressUpdate(long contentLength, long curLength) {

                }
            });
            Response response = httpEngine.execute();
            obj = callback.parse(response);
            obj = callback.postRequest(obj);
            sendMessageToMainThread(HttpConstants.SUCCESS, obj);
        } catch (HttpException e) {
            sendMessageToMainThread(HttpConstants.FAIL, e);
        } catch (Exception e) {
            sendMessageToMainThread(HttpConstants.FAIL, new HttpException(HttpException.ErrorType.UNKNOW, e.getMessage()));
        }
    }

    public void startExecuteInCurrentThread() {
        nRequestTaskListener.onPreExecute(mRequest.tag);
        try {
            Object obj = callback.preRequest(mRequest);
            if (obj != null) {
                callback.onSuccess(obj);
                return;
            }
            if (httpEngine == null) {
                httpEngine = new HttpURLConnectionHttpEngine();
            }
            httpEngine.setRequest(mRequest);
            httpEngine.setOnProgressUpdateListener(new OnProgressUpdateListener() {
                @Override
                public void onProgressUpdate(long contentLength, long curLength) {

                }
            });
            Response response = httpEngine.execute();
            obj = callback.parse(response);
            obj = callback.postRequest(obj);
            callback.onSuccess(obj);
        } catch (HttpException e) {
            callback.onFailure(e);
        } catch (Exception e) {
            callback.onFailure(new HttpException(HttpException.ErrorType.UNKNOW, e.getMessage()));
        }
    }

    /**
     * 子线程到主线程切换
     *
     * @param what 消息类型
     * @param obj  消息内容
     */
    private void sendMessageToMainThread(int what, Object obj) {
        Message message = Message.obtain();
        message.what = what;
        message.obj = obj;
        mHandler.sendMessage(message);
    }

    public void setOnRequestTaskListener(OnRequestTaskListener listener) {
        this.nRequestTaskListener = listener;
    }

    public void setOnGlobalExceptionListener(OnGlobalExceptionListener onGlobalExceptionListener) {
        this.onGlobalExceptionListener = onGlobalExceptionListener;
    }

    public void cancel() {
        mRequest.isCancelled = true;
        callback.cancel();
    }

    public void setHttpEngine(HttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }

    public interface OnRequestTaskListener {
        void onPreExecute(String tag);

        void onExecuteCompleted(String tag);
    }
}
