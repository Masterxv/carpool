package com.campussay.carpool.net.app_net;

import com.campussay.carpool.net.IOKClient;
import com.campussay.carpool.net.IServiceProvider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * create by zuyuan on 2019/4/4
 */
public class GeneralServiceProvider implements IServiceProvider<ApiService> {

    @Override
    public ApiService getService(String baseUrl, IOKClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CarpoolConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.getOKClient())
                .build()
                .create(ApiService.class);
    }
}
