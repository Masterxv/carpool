package com.campussay.carpool.ui.chat.manager;

import android.support.annotation.Nullable;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.ui.chat.bean.PlaceChatBean;
import com.campussay.carpool.ui.chat.other.ChatMessage;
import com.campussay.carpool.ui.chat.other.MType;
import com.campussay.carpool.utils.LogUtils;
import com.campussay.carpool.utils.NetStateUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * create by zuyuan on 2019/4/16
 * 地点聊天室
 */
public class PlaceChatManager extends BaseChatManager {

    public static final String PLACE_CHAT_URL = "ws://pin.varbee.com/chatRoom/tadpole/group";

    private volatile boolean mCanSend = false;

    public PlaceChatManager(String token, long selfId, String selfHeadUrl,
                            MessageListener listener) {
        super(token, selfId,selfHeadUrl, listener);
    }

    public List<ChatMessage> parseGson(String text) {
        List<ChatMessage> msg = new ArrayList<>();
        if (text.length() >= 0 && text.charAt(0) == '[') {
            Type type = new TypeToken<List<PlaceChatBean>>(){}.getType();
            List<PlaceChatBean> list = mGson.fromJson(text, type);
            for (PlaceChatBean b : list) {
                msg.add(parseOne(b));
            }
        } else {
            PlaceChatBean b =  mGson.fromJson(text, PlaceChatBean.class);
            msg.add(parseOne(b));
        }
        return msg;
    }

    private ChatMessage parseOne(PlaceChatBean b) {
        ChatMessage m = ChatMessage.obtain();
        m.selfId = getSelfId();
        m.date = b.getCreate();
        m.text = b.getContent();
        if (b.isOwn()) {
            m.type = MType.TYPE_RIGHT_TEXT;
            m.headUrl = getSelfHeadUrl();
        } else {
            m.type = MType.TYPE_LEFT_TEXT;
            m.headUrl = b.getAvatar();
        }
        return m;
    }

    @Override
    public ChatMessage sendMessage(String text) {
        super.sendMessage(text);
        ChatMessage m = ChatMessage.obtain();
        boolean sendSuccess = false;
        if (mWebSocket == null) {
            initWebSocket();
            sendSuccess = mWebSocket.send(text);
        } else {
            if (!(sendSuccess = mWebSocket.send(text))) {
                mWebSocket.cancel();
                initWebSocket();
            }
        }
        //没得网络则发送失败
        if (sendSuccess) sendSuccess = mCanSend;
        if (sendSuccess) sendSuccess =
                NetStateUtil.isNetworkConnected(CarpoolApplication.getApplication());
        //地点聊天会把消息返回
        if (sendSuccess) return null;
        m.headUrl = getSelfHeadUrl();
        m.type = MType.TYPE_RIGHT_TEXT;
        m.failInSend = false;
        m.selfId = getSelfId();
        m.text = text;
        m.date = new Date().getTime();
        return m;
    }

    @Override
    WebSocketListener createSocketListener(MessageListener listener) {
        return new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                LogUtils.w("socket--->open");
                mCanSend = true;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                LogUtils.d("socket--->message + " + text);
                List<ChatMessage> list = parseGson(text);
                if (list.size() == 1) {
                    mUiHandler.post(() -> mListener.receivedMsg(list.get(0)));
                } else {
                    mUiHandler.post(() -> mListener.receivedMsg(list));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                LogUtils.d("socket--->message + " + bytes.utf8());
                List<ChatMessage> list = parseGson(bytes.utf8());
                if (list.size() == 1) {
                    mUiHandler.post(() -> mListener.receivedMsg(list.get(0)));
                } else {
                    mUiHandler.post(() -> mListener.receivedMsg(list));
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                LogUtils.w("socket--->closed + " + reason);
                mCanSend = false;
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                LogUtils.w("socket--->fail + " + t.getMessage());
                mUiHandler.post(() -> mListener.fail());
                mCanSend = false;
            }
        };
    }

    @Override
    public void findOldMessagesByNet(long msgId, RequestMessageCallBack callBack) {
        callBack.onFail();
    }

    @Override
    public void findOldMessagesByDataBase(RequestMessageCallBack callBack) {
        callBack.onFail();
    }

    @Override
    public void refreshDataBaseWithRecycleMsg(List<ChatMessage> messages) { }


    @Override
    public String getCompleteUrl() {
        return new StringBuilder(PLACE_CHAT_URL)
                .append("/")
                .append(getToken())
                .toString();
    }
}
