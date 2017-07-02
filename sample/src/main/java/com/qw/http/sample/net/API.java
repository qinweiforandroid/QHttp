package com.qw.http.sample.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by qinwei on 2017/6/10.
 */

public class API {
    public static final String DOMAIN = "http://gank.io/api/data";

    public static String PICTURE = "/%E7%A6%8F%E5%88%A9";
    public static String ANDOIRD = "/Android";
    public static String IOS = "/iOS";

    public static String getDomain() {
        return DOMAIN;
    }

    public static String loadPictureList(int size, int page) {
        return getDomain() + PICTURE + "/" + size + "/" + page;
    }

    public static String loadAndroidList(int size, int page) {
        return getDomain() + ANDOIRD + "/" + size + "/" + page;
    }

    public static String loadIosList(int size, int page) {
        return getDomain() + IOS + "/" + size + "/" + page;
    }
}
