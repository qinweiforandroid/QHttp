package com.qw.http.sample;

import com.qw.http.anno.Param;
import com.qw.http.anno.API;
import com.qw.http.core.RequestMethod;

/**
 * Created by qinwei on 2017/11/25.
 */

public interface ApiService {
    @API(url = "https://www.baidu.com", method = RequestMethod.GET)
    void loadBaiDu(@Param("username") String username, @Param("password") String password);
}
