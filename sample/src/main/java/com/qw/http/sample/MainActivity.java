package com.qw.http.sample;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qw.http.HttpExecutor;
import com.qw.http.RequestManager;
import com.qw.http.callback.FileCallback;
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
                .setSafeInterface(new AesSafeImpl())
                .builder());
        requestPermission();
    }

    public void requestPermission() {
        String permissions[] = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        PermissionHelper.requestPermission(this, permissions, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mHttpGetBtn:
//                get();
//                getJsonToObject();
//                testPut();
//                testRequest();
//                executeInMainThread();
                testDownload();
                break;
            case R.id.mHttpCancelBtn:
                cancel("baidu");
                break;
            default:
                break;
        }
    }

    private void testDownload() {
        String download = "https://bccb3cda0187702ddb0619bf2363470e.dd.cdntips.com/imtt.dd.qq.com/16891/AB3A8795AB50248909371866CF73AD1B.apk?mkey=5c4695b574e47cdb&f=0af0&fsname=org.vv.brainTwister_3.98_128.apk&csr=1bbd&cip=116.228.90.46&proto=https";
        Request request = new Request(download);
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + request.toString()+".apk";
        RequestManager.getInstance().execute(request, new FileCallback(path) {
            @Override
            public void onSuccess(String path) {
                HttpLog.d("onSuccess:" + path);
            }

            @Override
            public void onFailure(HttpException httpException) {
                HttpLog.d("onFailure:" + httpException.getMsg());
            }

            @Override
            public void onProgressUpdate(long curLength, long contentLength) {
                HttpLog.d("onProgressUpdate:" + curLength + "/" + contentLength);
            }
        });
    }

    private void executeInMainThread() {
        new Thread() {
            @Override
            public void run() {
                HttpLog.d("executeInMainThread start");
                Request request = new Request(API.loadPictureList(20, 2));
                request.tag = "baidu";
                RequestManager.getInstance().executeInMainThread(request, new GankIOCallback<ArrayList<Meizhi>>() {
                    @Override
                    public void onSuccess(ArrayList<Meizhi> meizhis) {
                        HttpLog.d(meizhis.toString());
                    }

                    @Override
                    public void onFailure(HttpException httpException) {
                        httpException.printStackTrace();
                    }
                });
                HttpLog.d("executeInMainThread end");
            }
        }.start();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                boolean grant = true;
                for (int grantResult : grantResults) {
                    if (grantResult != 0) {
                        grant = false;
                    }
                }
                if (grant) {
                } else {
                    // Permission Denied
                    requestPermission();
                    Toast.makeText(this, "READ_PHONE_STATE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
