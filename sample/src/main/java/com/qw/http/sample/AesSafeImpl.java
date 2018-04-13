package com.qw.http.sample;

import com.qw.http.safe.SafeInterface;

/**
 * Created by qinwei on 2018/4/12.
 */

public class AesSafeImpl implements SafeInterface {
    @Override
    public String encrypt(String content) {
        return SafeUtil.encrypt("GnY5iyuhj6BPfDSG", content);
    }

    @Override
    public String decrypt(String content) {
        return SafeUtil.encrypt("GnY5iyuhj6BPfDSG", content);
    }
}
