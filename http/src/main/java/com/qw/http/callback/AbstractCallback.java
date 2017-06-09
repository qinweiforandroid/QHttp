package com.qw.http.callback;

import com.qw.http.core.Request;
import com.qw.http.core.Response;
import com.qw.http.exception.HttpException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by qinwei on 2017/6/9.
 */

public abstract class AbstractCallback<T> implements ICallback<T> {
    @Override
    public T parse(Response response) throws HttpException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = response.inputStream;
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            byte[] bfs = os.toByteArray();
            return convert(new String(bfs, "utf-8"));
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    public abstract T convert(String content);

    @Override
    public T preRequest(Request mRequest) {
        return null;
    }

    @Override
    public T postRequest(T t) {
        return t;
    }
}
