package com.qw.http.sample;

import com.google.gson.Gson;
import com.qw.http.core.AbstractCallback;
import com.qw.http.exception.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by qinwei on 2017/7/2.
 */

public abstract class GankIOCallback<T> extends AbstractCallback<T> {
    @Override
    public T convert(String content) throws HttpException {
        try {
            JSONObject json = new JSONObject(content);
            boolean error = json.getBoolean("error");
            if (!error) {
                Object data = json.opt("results");
                Gson gson = new Gson();
                Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                return gson.fromJson(data.toString(), type);
            } else {
                throw new HttpException(HttpException.ErrorType.SERVER, content);
            }
        } catch (JSONException e) {
            throw new HttpException(HttpException.ErrorType.JSON, e.getMessage());
        }
    }
}
