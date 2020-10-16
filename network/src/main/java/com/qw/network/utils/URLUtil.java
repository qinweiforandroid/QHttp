package com.qw.network.utils;

/**
 * Created by qinwei on 2020/7/10 4:32 PM
 * email: qinwei_it@163.com
 */
public class URLUtil {
    /**
     * @return {@code true} if the url is an http: url.
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) &&
                (url.length() > 6) &&
                url.substring(0, 7).equalsIgnoreCase("http://");
    }

    /**
     * @return {@code true} if the url is an https: url.
     */
    public static boolean isHttpsUrl(String url) {
        return (null != url) &&
                (url.length() > 7) &&
                url.substring(0, 8).equalsIgnoreCase("https://");
    }

    /**
     * @return {@code true} if the url is a network url.
     */
    public static boolean isNetworkUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return isHttpUrl(url) || isHttpsUrl(url);
    }
}
