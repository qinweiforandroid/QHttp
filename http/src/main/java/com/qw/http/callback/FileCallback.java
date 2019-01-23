package com.qw.http.callback;

import com.qw.http.core.AbstractCallback;
import com.qw.http.core.OnProgressUpdateListener;
import com.qw.http.core.Response;
import com.qw.http.exception.HttpException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by qinwei on 2019/1/23 4:33 PM
 * email: qin.wei@mwee.cn
 */

public abstract class FileCallback extends AbstractCallback<String> implements OnProgressUpdateListener {
    private String path;

    public FileCallback(String path) {
        this.path = path;
    }

    @Override
    public String parse(Response response) throws HttpException {
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            InputStream is = response.inputStream;
            byte[] buffer = new byte[2048];
            int len = -1;
            long contentLength = response.getContentLength();
            long curPos = 0;
            while ((len = is.read(buffer)) != -1) {
                checkIfCancelled();
                fos.write(buffer, 0, len);
                curPos += len;
                onProgressUpdate(curPos, contentLength);
            }
            onProgressUpdate(contentLength, contentLength);
            is.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public String convert(String content) throws HttpException {
        return path;
    }

    public FileCallback setDownloadPath(String path) {
        this.path = path;
        return this;
    }
}
