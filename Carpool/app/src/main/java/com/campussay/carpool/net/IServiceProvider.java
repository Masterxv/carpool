package com.campussay.carpool.net;

/**
 * create by zuyuan on 2019/4/4
 */
public interface IServiceProvider<T> {
    T getService(String baseUrl, IOKClient client);
}
