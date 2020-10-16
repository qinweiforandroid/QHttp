package com.qw.http.core;

import com.qw.http.safe.DefaultSafeImpl;
import com.qw.http.safe.SafeInterface;
import com.qw.http.utils.HttpConstants;

/**
 * http请求配置信息
 * Created by qinwei on 2018/1/14.
 */
public class RequestConfig {
    /**
     * 延迟执行时间
     */
    private int delayTime;
    /**
     * 连接超时时间
     */
    private int connect_timeout;
    /**
     * 响应超时时间
     */
    private int read_timeout;
    /**
     * 请求处理工具
     */
    private Engine engine;

    /**
     * 加解密配置
     */
    private SafeInterface safeInterface;

    /**
     * RequestConfig构建工具类
     */
    public static class Builder {
        private int delayTime;
        private int connect_timeout;
        private int read_timeout;
        private Engine engine;
        private SafeInterface safeInterface;

        public Builder setConnectTimeout(int time) {
            this.connect_timeout = time;
            return this;
        }

        public Builder setReadTimeout(int time) {
            this.read_timeout = time;
            return this;
        }

        public Builder setDelayTime(int time) {
            this.delayTime = time;
            return this;
        }

        public Builder setEngine(Engine engine) {
            this.engine = engine;
            return this;
        }

        public Builder setSafeInterface(SafeInterface safeInterface) {
            this.safeInterface = safeInterface;
            return this;
        }

        public RequestConfig builder() {
            RequestConfig config = new RequestConfig();
            if (connect_timeout == 0) {
                connect_timeout = HttpConstants.TIME_OUT;
            }
            config.connect_timeout = connect_timeout;
            if (read_timeout == 0) {
                read_timeout = HttpConstants.TIME_OUT;
            }
            config.read_timeout = read_timeout;
            config.delayTime = delayTime;
            if (engine == null) {
                engine = new HttpURLConnectionEngine();
            }
            config.engine = engine;
            if (safeInterface == null) {
                safeInterface = new DefaultSafeImpl();
            }
            config.safeInterface = safeInterface;
            return config;
        }
    }

    public static RequestConfig getDefault() {
        return new Builder()
                .setConnectTimeout(HttpConstants.TIME_OUT)
                .setReadTimeout(HttpConstants.TIME_OUT)
                .setDelayTime(0)
                .builder();
    }

    public int getDelayTime() {
        return delayTime;
    }

    public int getConnectTimeout() {
        return connect_timeout;
    }

    public int getReadTimeout() {
        return read_timeout;
    }

    public Engine getEngine() {
        return engine;
    }

    public SafeInterface getSafeInterface() {
        return safeInterface;
    }
}
