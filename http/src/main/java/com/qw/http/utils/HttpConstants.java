package com.qw.http.utils;

import okhttp3.MediaType;

/**
 * Created by qinwei on 2017/6/8.
 */

public class HttpConstants {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int TIME_OUT = 5000;
}
