package com.campussay.carpool.net.app_socket;

import com.campussay.carpool.net.BaseANInterceptor;
import com.campussay.carpool.net.IOKClient;
import com.campussay.carpool.net.app_net.GeneralInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * create by zuyuan on 2019/4/5
 */
public class SocketOKClient implements IOKClient {

    private BaseANInterceptor mInterceptor = new GeneralInterceptor();

    private final int mConnectTime;

    public SocketOKClient() {
        mConnectTime = 10;
    }

    @Override
    public OkHttpClient getOKClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(mInterceptor.getAppInterceptor())
                .addNetworkInterceptor(mInterceptor.getNetInterceptor())
                .connectTimeout(mConnectTime, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Override
    public int getConnectTimeOut() {
        return mConnectTime;
    }

    @Override
    public IOKClient setInterceptor(BaseANInterceptor interceptor) {
        return this;
    }
}
