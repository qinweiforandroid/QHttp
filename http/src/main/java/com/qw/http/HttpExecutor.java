package com.qw.http;

import com.qw.http.anno.Parm;
import com.qw.http.anno.Url;
import com.qw.http.core.ICallback;
import com.qw.http.core.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by qinwei on 2017/11/25.
 */

public class HttpExecutor {
    public static <T, D> T create(Class<T> clz, final ICallback<D> callback) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class<?>[]{clz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Request request = null;
                if (method.isAnnotationPresent(Url.class)) {
                    Url url = method.getAnnotation(Url.class);
                    request = new Request(url.url(), url.method());
                    Annotation[][] annotations = method.getParameterAnnotations();
                    for (int i = 0; i < annotations.length; i++) {
                        for (Annotation anno : annotations[i]) {
                            if (anno instanceof Parm) {
                                String key = ((Parm) anno).value();
                                if (args[i] instanceof String) {
                                    request.put(key, args[i].toString());
                                } else if (args[i] instanceof Integer) {
                                    request.put(key, (String) args[i]);
                                }
                            }
                        }
                    }
                }
                RequestManager.getInstance().execute(request, callback);
                return null;
            }
        });
    }
}
