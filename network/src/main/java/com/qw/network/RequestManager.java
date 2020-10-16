package com.qw.network;

import com.qw.network.core.ICallback;
import com.qw.network.core.IRequestEngine;
import com.qw.network.core.Request;

/**
 * Created by qinwei on 2020/7/10 2:36 PM
 * email: qinwei_it@163.com
 */
public class RequestManager {
    private static IRequestEngine engine;
    private static Config config;

    public static void inject(IRequestEngine requestEngine, Config config) {
        engine = requestEngine;
        RequestManager.config = config;
    }

    public static <T> void get(Request request, ICallback<T> callback) {
        engine.get(request, config, callback);
    }

    public static <T> void post(Request request, ICallback<T> callback) {
        engine.get(request, config, callback);
    }

    public static <T> void put(Request request, ICallback<T> callback) {
        engine.get(request, config, callback);
    }

    public static <T> void delete(Request request, ICallback<T> callback) {
        engine.get(request, config, callback);
    }
}
