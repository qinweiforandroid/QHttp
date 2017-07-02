package com.qw.http.callback;

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

public abstract class JsonCallback<T> extends AbstractCallback<T> {
    @Override
    public T convert(String content) throws HttpException {
        try {
            JSONObject json = new JSONObject(content);
            Object data = json.opt("data");
            Gson gson = new Gson();
            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            throw new HttpException(HttpException.ErrorType.JSON, e.getMessage());
        }
    }
}
