package com.qw.http.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qw.http.RequestManager;
import com.qw.http.callback.OnGlobalExceptionListener;
import com.qw.http.callback.StringCallback;
import com.qw.http.core.Request;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnGlobalExceptionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mHttpGetBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mHttpGetBtn:
                get();
                break;
        }
    }

    private void get() {
        Request request = new Request("http://www.cnblogs.com/ddddfpxx/p/6329407.html");
        request.tag = "baidu";
        request.addHeader("contentType", "application/json");
        request.addHeader("token", "abc");
        request.put("username", "qinwei");
        request.put("password", "123456");
        RequestManager.getInstance().setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute(request, new StringCallback() {
            @Override
            public void onSuccess(String s) {
                HttpLog.d("onSuccess:" + s);
            }

            @Override
            public void onFailure(HttpException httpException) {

            }
        });
    }

    @Override
    public boolean onHandlerGlobalException(HttpException e) {
        return false;
    }
}
