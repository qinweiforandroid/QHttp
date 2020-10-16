package com.qw.network;

import com.qw.network.core.Request;
import com.qw.network.core.StringCallback;
import com.qw.network.log.HttpLog;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
        HttpLog.d("test");
        RequestManager.inject(new HttpURLConnectionRequestEngine(), new Config());
        Request request = new Request("https://gank.io/api/v2/banners");
        request.sync = true;
        RequestManager.get(request, new StringCallback() {
            @Override
            public void onSuccess(String s) {
                HttpLog.d("onSuccess");
            }

            @Override
            public void onFailure(HttpException e) {
                System.out.println("code:" + e.getCode() + ",msg:" + e.getMsg());
            }
        });
    }
}