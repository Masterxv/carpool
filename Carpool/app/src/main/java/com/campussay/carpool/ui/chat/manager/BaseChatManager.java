package com.campussay.carpool.ui.chat.manager;

import android.os.Handler;
import android.os.Looper;

import com.campussay.carpool.net.app_socket.SocketClient;
import com.campussay.carpool.ui.chat.other.ChatMessage;
import com.campussay.carpool.utils.LogUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * create by zuyuan on 2019/4/16
 */
public abstract class BaseChatManager {

    Gson mGson;

    Executor mExecutor;

    WebSocket mWebSocket;

    KeepHeartBeat mHeartBeat;

    MessageListener mListener;

    final Handler mUiHandler;

    private long mSelfId;

    private String mToken;

    private String mSelfHeadUrl;

    public BaseChatManager(String token, long selfId,
                           String selfHeadUrl, MessageListener listener) {
        setToken(token);
        setSelfId(selfId);
        setSelfHeadUrl(selfHeadUrl);
        mListener = listener;
        mGson = new Gson();
        mHeartBeat = new KeepHeartBeat();
        mExecutor = Executors.newSingleThreadExecutor();
        mUiHandler = new Handler(Looper.getMainLooper());

        mHeartBeat.register(this);
    }

    public String getSelfHeadUrl() {
        return mSelfHeadUrl;
    }

    public void setSelfHeadUrl(String selfHeadUrl) {
        this.mSelfHeadUrl = selfHeadUrl;
    }

    public WebSocket getWebSocket() {
            return mWebSocket;
        }

    public long getSelfId() {
        return mSelfId;
    }

    public void setSelfId(long mId) {
        this.mSelfId = mId;
    }

    public String getToken() {
            return mToken;
        }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public void setMessageListener(MessageListener listener) {
        mListener = listener;
    }

    public boolean isOpenWebSocket() {
        return mWebSocket != null;
    }

    public void sendMessageWithToken() {
        LogUtils.d("WebSocket发送token——保持心跳");
        boolean send = mWebSocket.send(mToken);
        if (!send) initWebSocket();
    }

    void initWebSocket() {
        mWebSocket = SocketClient.createNewSocket(getCompleteUrl(),
                createSocketListener(mListener));
    }

    public void openSocket() {
        initWebSocket();
    }

    public void closeSocket() {
        if (mWebSocket != null) {
            mWebSocket.cancel();
            mWebSocket = null;
        }
    }

    public void detach() {
        if (mWebSocket != null) {
            mWebSocket.cancel();
            mWebSocket = null;
        }
        mHeartBeat.unRegister();
    }

    /**
     * 更新抽象方法，绑定“心跳”
     */
    public ChatMessage sendMessage(String text) {
        mHeartBeat.bindOnSendMessage();
        return null;
    }

    abstract WebSocketListener createSocketListener(MessageListener listener);

    public abstract void findOldMessagesByNet(long msgId, RequestMessageCallBack callBack);

    public abstract void findOldMessagesByDataBase(RequestMessageCallBack callBack);

    public abstract void refreshDataBaseWithRecycleMsg(List<ChatMessage> messages);

    public abstract String getCompleteUrl();
}
