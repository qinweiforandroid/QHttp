package com.qw.http.safe;

/**
 * 默认不加密也不解密
 * Created by qinwei on 2018/4/11.
 */

public class DefaultSafeImpl implements SafeInterface {
    @Override
    public String encrypt(String content) {
        return content;
    }

    @Override
    public String decrypt(String content) {
        return content;
    }
}
