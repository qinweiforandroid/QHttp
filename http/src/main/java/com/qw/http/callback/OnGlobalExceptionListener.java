package com.qw.http.callback;

import com.qw.http.exception.HttpException;

/**
 * Created by qinwei on 2017/6/9.
 */

public interface OnGlobalExceptionListener {
    boolean onHandlerGlobalException(HttpException e);
}
