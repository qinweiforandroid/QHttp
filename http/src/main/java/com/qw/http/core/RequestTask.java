package com.qw.http.core;

import android.os.Handler;
import android.os.Message;

import com.qw.http.callback.ICallback;
import com.qw.http.callback.OnGlobalExceptionListener;
import com.qw.http.callback.OnProgressUpdateListener;
import com.qw.http.exception.HttpException;
import com.qw.http.utils.HttpConstants;

/**
 * Created by qinwei on 2017/6/8.
 */

public class RequestTask implements Runnable {
    private Request mRequest;
    private ICallback callback;
    private OnRequestTaskListener nRequestTaskListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
    private OnGlobalExceptionListener onGlobalExceptionListener;

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
                Message message = new Message();
                message.what = HttpConstants.SUCCESS;
                message.obj = obj;
                mHandler.sendMessage(message);
                return;
            }
            HttpEngine httpEngine = new HttpURLConnectionHttpEngine();
            httpEngine.setRequest(mRequest);
            httpEngine.setOnProgressUpdateListener(new OnProgressUpdateListener() {
                @Override
                public void onProgressUpdate(long contentLength, long curLength) {

                }
            });
            Response response = httpEngine.execute();
            obj = callback.parse(response);
            obj = callback.postRequest(obj);
            Message message = new Message();
            message.what = HttpConstants.SUCCESS;
            message.obj = obj;
            mHandler.sendMessage(message);
        } catch (HttpException e) {
            Message message = new Message();
            message.what = HttpConstants.FAIL;
            message.obj = e;
            mHandler.sendMessage(message);
        }
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

    public interface OnRequestTaskListener {
        void onPreExecute(String tag);

        void onExecuteCompleted(String tag);
    }
}
