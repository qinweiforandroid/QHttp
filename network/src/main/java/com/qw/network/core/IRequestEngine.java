package com.qw.network.core;

import com.qw.network.Config;

/**
 * Created by qinwei on 2020/7/10 2:39 PM
 * email: qinwei_it@163.com
 */
public interface IRequestEngine {
   <T> void get(Request request, Config config, ICallback<T> callback);

    void cancel(String tag);

    void cancelAll();
}
