package com.qw.http.core;

import com.qw.http.exception.HttpException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by qinwei on 2017/6/9.
 */

public abstract class AbstractCallback<T> implements ICallback<T> {
    private volatile boolean isCancelled;

    @Override
    public T parse(Response response) throws HttpException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = response.inputStream;
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) > 0) {
                checkIfCancelled();
                os.write(buffer, 0, len);
            }
            byte[] bfs = os.toByteArray();
            is.close();
            return convert(new String(bfs, "utf-8"));
        } catch (IOException e) {
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    public abstract T convert(String content) throws HttpException;

    @Override
    public T preRequest(Request mRequest) {
        return null;
    }

    @Override
    public T postRequest(T t) {
        return t;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    /**
     * 检测请求是否被取消
     *
     * @throws HttpException
     */
    private void checkIfCancelled() throws HttpException {
        if (isCancelled) {
            throw new HttpException(HttpException.ErrorType.CANCEL, "the request has been cancelled");
        }
    }
}
