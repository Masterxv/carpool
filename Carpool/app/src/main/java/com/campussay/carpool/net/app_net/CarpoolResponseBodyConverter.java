package com.campussay.carpool.net.app_net;

import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * create by WenJinG on 2019/4/21
 */
public class CarpoolResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CarpoolResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        BaseBean baseBean = gson.fromJson(response,BaseBean.class);
        if(!baseBean.isSuccess()){
            TypeToken<BaseBean<RequestErrorBean>>typeToken = new TypeToken<BaseBean<RequestErrorBean>>(){};
            BaseBean<RequestErrorBean>errorBaseBean= gson.fromJson(response,typeToken.getType());
            throw new RequestErrorException(errorBaseBean.getMessage(),errorBaseBean.getData());
        }
        try {
             MediaType contentType = value.contentType();
             Charset charset =  (contentType != null) ? contentType.charset(UTF_8) : UTF_8;
             InputStream inputStream = new ByteArrayInputStream(response.getBytes());
             InputStreamReader reader = new InputStreamReader(inputStream, charset);
             JsonReader jsonReader = gson.newJsonReader(reader);
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }


}
