package com.campussay.carpool.net.app_net;

import com.campussay.carpool.net.BaseANInterceptor;
import com.campussay.carpool.net.IOKClient;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * create by zuyuan on 2019/4/4
 */
public class GeneralOKClient implements IOKClient {

    private final int mConnectTimeOut;    //10s

    private final int mReadTimeOut;

    private final int mWriteTimeOut;

    private BaseANInterceptor mIntercept;

    public GeneralOKClient() {
        mConnectTimeOut = 10;
        mReadTimeOut = 5;
        mWriteTimeOut = 5;
    }

    @Override
    public OkHttpClient getOKClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (mIntercept != null) {
            Interceptor interceptor = mIntercept.getNetInterceptor();
            if (interceptor != null) {
                builder.addNetworkInterceptor(interceptor);
            }
            interceptor = mIntercept.getAppInterceptor();
            if (interceptor != null) {
                builder.addInterceptor(interceptor);
            }
        }
       return builder.connectTimeout(mConnectTimeOut, TimeUnit.SECONDS)
                .readTimeout(mReadTimeOut, TimeUnit.SECONDS)
                .writeTimeout(mWriteTimeOut, TimeUnit.SECONDS)
               .retryOnConnectionFailure(true)
               .build();

    }

    @Override
    public int getConnectTimeOut() {
        return mConnectTimeOut;
    }

    @Override
    public IOKClient setInterceptor(BaseANInterceptor interceptor) {
        mIntercept = interceptor;
        return this;
    }

}
