package com.qw.http.utils;

import com.qw.http.core.Request;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by qinwei on 2017/6/8.
 */

public class HttpURLConnectionUtil {
    public static HttpURLConnection execute(Request mRequest) throws HttpException {
        try {
            HttpURLConnection connection = null;
            URL url = new URL(mRequest.url);
            HttpLog.d("url:" + mRequest.url);
            if (mRequest.url.startsWith("https")) {
                connection = (HttpsURLConnection) url.openConnection();
                // FIXME: 2017/6/8 设置证书相关
            } else if (mRequest.url.startsWith("http")) {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setRequestMethod(mRequest.method.name());
            addHeaders(connection, mRequest.headers);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    /**
     * 设置http请求header
     *
     * @param connection
     * @param headers
     */
    private static void addHeaders(HttpURLConnection connection, HashMap<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }


}
