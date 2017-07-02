package com.qw.http.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qw.http.RequestManager;
import com.qw.http.callback.OnGlobalExceptionListener;
import com.qw.http.callback.StringCallback;
import com.qw.http.core.Request;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;
import com.qw.http.sample.net.API;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnGlobalExceptionListener {

    private Button mHttpGetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHttpGetBtn = (Button) findViewById(R.id.mHttpGetBtn);
        mHttpGetBtn.setOnClickListener(this);
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
        Request request = new Request(API.loadPictureList(20,2));
        request.tag = "baidu";
        RequestManager.getInstance().setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute(request, new StringCallback() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailure(HttpException httpException) {
                HttpLog.d(httpException.getMsg());
            }
        });
    }

    @Override
    public boolean onHandlerGlobalException(HttpException e) {
        return false;
    }
}
