package com.campussay.carpool.net;

import okhttp3.OkHttpClient;

/**
 * create by zuyuan on 2019/4/4
 */
public interface IOKClient {
    OkHttpClient getOKClient();
    int getConnectTimeOut();
    IOKClient setInterceptor(BaseANInterceptor interceptor);
}
