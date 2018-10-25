package com.qw.http.exception;

/**
 * Created by qinwei on 2017/6/8.
 */

public class HttpException extends Exception {
    public enum ErrorType {
        IO, TIMEOUT, JSON, FILE_NOT_FIND, CANCEL, SERVER, UPLOAD, UNKNOW
    }

    private ErrorType type;
    private int code;
    private String msg = "";

    public HttpException(ErrorType type, String detailMessage) {
        super(detailMessage);
        this.type = type;
        this.msg = detailMessage;
    }

    public HttpException(int code, String detailMessage) {
        super(detailMessage);
        this.type = ErrorType.SERVER;
        this.code = code;
        this.msg = detailMessage;
    }

    public ErrorType getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
