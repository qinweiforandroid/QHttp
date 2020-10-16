package com.qw.network.core;

import com.qw.network.HttpException;

import java.io.InputStream;

/**
 * Created by qinwei on 2020/7/10 2:39 PM
 * email: qinwei_it@163.com
 * 网络请求工作流
 * 1.request.sync=true 当前线程执行
 * 2.request.sync=false 在子线程执行
 *
 * @author qinwei
 */
public interface ICallback<T> {

    /**
     * 响应数据
     *
     * @param t
     */
    void onSuccess(T t);

    /**
     * 请求失败
     * 1.网络问题
     * 2.业务问题 ErrorType SERVER  code：服务器返回的业务code
     *
     * @param e
     */
    void onFailure(HttpException e);

    /**
     * 解析网络输入流
     *
     * @param is
     * @return
     * @throws HttpException
     */
    T parse(InputStream is) throws HttpException;

    /**
     * 解析错误的输入流
     *
     * @param is
     * @return
     * @throws HttpException
     */
    String error(InputStream is) throws HttpException;

    /**
     * 取消请求
     */
    void cancel();
}
