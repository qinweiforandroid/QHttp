package com.qw.network.log;

/**
 * Created by qinwei on 2017/6/8.
 */

public class HttpLog {
    public static final String TAG = "HttpLog";
    public static boolean DEBUG = true;
    public static String PREFIX = "-->";

    public static void d(String msg) {
        if (DEBUG) {
//            Log.d(TAG, PREFIX + msg);
            System.out.println(PREFIX + msg);
        }
    }
}
