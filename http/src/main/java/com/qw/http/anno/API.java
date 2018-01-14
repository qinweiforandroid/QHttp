package com.qw.http.anno;

import com.qw.http.core.RequestMethod;

/**
 * Created by qinwei on 2017/11/25.
 */

public @interface API {
    String url();

    RequestMethod method() default RequestMethod.GET;
}
