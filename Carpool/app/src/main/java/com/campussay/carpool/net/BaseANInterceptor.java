package com.campussay.carpool.net;

import com.campussay.carpool.utils.LogUtils;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * create by zuyuan on 2019/4/4
 */
public abstract class BaseANInterceptor {
    public abstract Interceptor getAppInterceptor();

    public abstract Interceptor getNetInterceptor();

    public static Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
