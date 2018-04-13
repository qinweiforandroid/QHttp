package com.qw.http.safe;

/**
 * 数据加解密接口
 * Created by qinwei on 2018/4/11.
 */

public interface SafeInterface {

    /**
     * 加密
     *
     * @param content 加密前数据
     * @return 加密后数据
     */
    String encrypt(String content);

    /**
     * 解密
     *
     * @param content 加密数据
     * @return 解密后数据
     */
    String decrypt(String content);
}
