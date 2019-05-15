package com.campussay.carpool.net.app_net;

import com.campussay.carpool.net.BaseANInterceptor;

import okhttp3.Interceptor;

/**
 * create by zuyuan on 2019/4/4
 */
public class GeneralInterceptor extends BaseANInterceptor {
    @Override
    public Interceptor getAppInterceptor() {
        return getLoggingInterceptor();
    }

    @Override
    public Interceptor getNetInterceptor() {
        return getLoggingInterceptor();
    }
}
