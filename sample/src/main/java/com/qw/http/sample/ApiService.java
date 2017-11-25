package com.qw.http.sample;

import com.qw.http.anno.Parm;
import com.qw.http.anno.Url;
import com.qw.http.core.RequestMethod;

/**
 * Created by qinwei on 2017/11/25.
 */

public interface ApiService {
    @Url(url = "https://www.baidu.com", method = RequestMethod.GET)
    void loadBaiDu(@Parm("username") String username, @Parm("password") String password);
}
