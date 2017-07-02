package com.qw.http.callback;

import com.qw.http.core.AbstractCallback;
import com.qw.http.log.HttpLog;

/**
 * Created by qinwei on 2017/6/9.
 */

public abstract class StringCallback extends AbstractCallback<String> {
    @Override
    public String convert(String content) {
        HttpLog.d("server response content:" + content);
        return content;
    }
}
