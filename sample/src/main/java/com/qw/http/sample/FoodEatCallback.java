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

public abstract class FoodEatCallback<T> extends AbstractCallback<T> {
    @Override
    public T convert(String content) throws HttpException {
        try {
            JSONObject json = new JSONObject(content);
            int errno = json.getInt("errno");
            if (errno == 0) {
                Object data = json.opt("data");
                Gson gson = new Gson();
                Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                return gson.fromJson(data.toString(), type);
            } else {
                throw new HttpException(errno, json.getString("errmsg"));
            }
        } catch (JSONException e) {
            throw new HttpException(HttpException.ErrorType.JSON, e.getMessage());
        }
    }

    @Override
    protected HttpException handlerError(int code, String content) {
        try {
            JSONObject obj = new JSONObject(content);
            int errno = obj.getInt("errno");
            String msg = obj.getString("errmsg");
            return super.handlerError(errno, msg);
        } catch (JSONException e) {
            return super.handlerError(code, content);
        }
    }
}
