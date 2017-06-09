package com.qw.http.log;

import android.util.Log;

/**
 * Created by qinwei on 2017/6/8.
 */

public class HttpLog {
    public static final String TAG = "HttpLog";

    public static void d(String msg) {
        Log.d(TAG, "-->" + msg);
    }
}
