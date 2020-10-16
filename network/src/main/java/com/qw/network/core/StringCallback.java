package com.qw.network.core;

/**
 * Created by qinwei on 2020/7/10 4:21 PM
 * email: qinwei_it@163.com
 */
public abstract class StringCallback extends AbstractCallback<String> {
    @Override
    protected String convert(String content) {
        return content;
    }
}
