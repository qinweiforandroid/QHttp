package com.qw.http.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinwei on 2017/6/9.
 */

public class HttpStringUtil {
    public static String buildCompletedUrl(String url, HashMap<String, String> parameters) {
        if (url.contains("?")) {
            url += buildParameterContent(parameters);
        } else {
            url += "?" + buildParameterContent(parameters);
        }
        return url;
    }

    public static String buildParameterContent(HashMap<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue());
            builder.append("&");
        }
        return builder.substring(0, builder.length() - 1);
    }
}
