package com.campussay.carpool.net.app_socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * create by zuyuan on 2019/4/5
 */
public class SocketClient {
    public static synchronized WebSocket createNewSocket(String url, WebSocketListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        SocketOKClient client = new SocketOKClient();
        OkHttpClient ok =  client.getOKClient();
        WebSocket socket =  ok.newWebSocket(request, listener);
        ok.dispatcher().executorService().shutdown();
        return socket;
    }

    public static synchronized WebSocket createNewSocket(Request request, WebSocketListener listener) {
        SocketOKClient client = new SocketOKClient();
        OkHttpClient ok =  client.getOKClient();
        WebSocket socket =  ok.newWebSocket(request, listener);
        ok.dispatcher().executorService().shutdown();
        return socket;    }

    /*
      用法:
      SocketClient.createNewSocket();   //创建一个新的WebSocket，注意不是单例
     */
}
