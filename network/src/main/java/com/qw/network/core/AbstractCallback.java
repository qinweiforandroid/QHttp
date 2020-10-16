package com.qw.network.core;

import com.qw.network.HttpException;
import com.qw.network.log.HttpLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by qinwei on 2020/7/10 4:16 PM
 * email: qinwei_it@163.com
 */
public abstract class AbstractCallback<T> implements ICallback<T> {
    private boolean isCancelled = false;

    @Override
    public T parse(InputStream is) throws HttpException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
                checkCancelled();
            }
            byte[] bfs = os.toByteArray();
            is.close();
            os.close();
            String content = new String(bfs, "utf-8");
            HttpLog.d("responseContent:" + content);
            return convert(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    protected abstract T convert(String content);

    @Override
    public String error(InputStream is) throws HttpException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
                checkCancelled();
            }
            byte[] bfs = os.toByteArray();
            is.close();
            os.close();
            String content = new String(bfs, "utf-8");
            checkCancelled();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException(HttpException.ErrorType.IO, e.getMessage());
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    protected void checkCancelled() throws HttpException {
        if (isCancelled) {
            throw new HttpException(HttpException.ErrorType.CANCEL, "cancelled");
        }
    }
}