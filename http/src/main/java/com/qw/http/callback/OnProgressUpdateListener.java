package com.qw.http.callback;

/**
 * Created by qinwei on 2017/6/9.
 */

public interface OnProgressUpdateListener {
    void onProgressUpdate(long contentLength, long curLength);
}
