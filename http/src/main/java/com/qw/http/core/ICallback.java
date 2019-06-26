package com.qw.http.core;

import com.qw.http.exception.HttpException;

/**
 * http请求过程回调函数
 * Created by qinwei on 2017/6/8.
 */

public interface ICallback<T> {

    T parse(Response response) throws HttpException;

    /**
     * call from main thread you can execute update ui task
     *
     * @param t http 请求响应数据
     */
    void onSuccess(T t);

    /**
     * http 请求过程中出现异常回调方法
     *
     * @param httpException h
     */
    void onFailure(HttpException httpException);

    /**
     * http 请求前执行的任务 call in subThread you can execute long time task example read db
     *
     * @param mRequest r
     * @return if return null then will call {@link ICallback onSuccess method}
     */
    T preRequest(Request mRequest);

    /**
     * http 请求结束后执行的任务 call in subThread  execute completed will call onSuccess method
     *
     * @param t http响应处理后的数据
     * @return t
     */
    T postRequest(T t);

    void cancel();

}
