package com.qw.http.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.qw.http.HttpExecutor;
import com.qw.http.RequestManager;
import com.qw.http.core.HttpURLConnectionHttpEngine;
import com.qw.http.core.OnGlobalExceptionListener;
import com.qw.http.callback.StringCallback;
import com.qw.http.core.Request;
import com.qw.http.core.RequestConfig;
import com.qw.http.core.RequestMethod;
import com.qw.http.exception.HttpException;
import com.qw.http.log.HttpLog;
import com.qw.http.sample.domain.Meizhi;
import com.qw.http.sample.net.API;
import com.qw.http.utils.HttpConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnGlobalExceptionListener {

    private Button mHttpGetBtn;
    private Button mHttpCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHttpGetBtn = (Button) findViewById(R.id.mHttpGetBtn);
        mHttpGetBtn.setOnClickListener(this);
        mHttpCancelBtn = (Button) findViewById(R.id.mHttpCancelBtn);
        mHttpCancelBtn.setOnClickListener(this);
        RequestManager.getInstance().config(new RequestConfig.Builder()
                .setConnectTimeout(HttpConstants.TIME_OUT)
                .setReadTimeout(HttpConstants.TIME_OUT)
                .setDelayTime(0)
                .setHttpEngine(new HttpURLConnectionHttpEngine())
                .builder());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mHttpGetBtn:
//                get();
                getJsonToObject();
//                testPut();
//                testRequest();
                break;
            case R.id.mHttpCancelBtn:
                cancel("baidu");
                break;
            default:
                break;
        }
    }

    private void testRequest() {
        HttpExecutor.create(ApiService.class, new StringCallback() {
            @Override
            public void onSuccess(String s) {
                HttpLog.d(s);
            }

            @Override
            public void onFailure(HttpException httpException) {

            }
        }).loadBaiDu("1", "2");
    }

    private void testPut() {
        try {
            Request request = new Request("https://api.bmob.cn/1/users/a8d23440cd", RequestMethod.PUT);
            request.addHeader("X-Bmob-Application-Id", "e576b3b89c2611b1e691a62914af5a80");
            request.addHeader("X-Bmob-REST-API-Key", "baae0b942cd77844447332dfaadb7c5b");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("X-Bmob-Session-Token", "3e33d8a840a9f177800dc1ad602207cf");
            JSONObject json = new JSONObject();
            json.put("nick", "11");
            request.postContent = json.toString();
            RequestManager.getInstance().execute(request, new StringCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.d("s", s);
                }

                @Override
                public void onFailure(HttpException httpException) {
                    httpException.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJsonToObject() {
        Request request = new Request(API.loadPictureList(20, 2));
        request.tag = "baidu";
        RequestManager.getInstance().execute(request, new GankIOCallback<ArrayList<Meizhi>>() {
            @Override
            public void onSuccess(ArrayList<Meizhi> meizhis) {
                HttpLog.d(meizhis.toString());
            }

            @Override
            public void onFailure(HttpException httpException) {
                httpException.printStackTrace();
            }
        });
    }

    private void cancel(String tag) {
        RequestManager.getInstance().cancel(tag);
    }

    private void get() {
        Request request = new Request(API.loadPictureList(20, 2));
        request.tag = "baidu";
        request.delayTime = 3000;
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
