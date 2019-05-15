package com.campussay.carpool.ui.chat.manager;

import android.support.annotation.Nullable;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.ui.chat.bean.GroupChatBean;
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
 * 群聊
 */
public class GroupChatManager extends BaseChatManager {

    public static final String BASE_GROUP_URL = "ws://pin.varbee.com/chatRoom/tadpole/team";

    private long mGroupId;

    private volatile boolean mCanSend = false;

    public GroupChatManager(String token, long selfId, long groupId,
                                String selfHeadUrl, MessageListener listener) {
            super(token, selfId, selfHeadUrl, listener);
            mGroupId = groupId;
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
    public ChatMessage sendMessage(String text) {
        super.sendMessage(text);
        ChatMessage m = ChatMessage.obtain();
        boolean sendSuccess;
        if (mWebSocket == null) {
            initWebSocket();
            sendSuccess = mWebSocket.send(text);
        } else {
            if (!(sendSuccess = mWebSocket.send(text))) {
                mWebSocket.cancel();
                initWebSocket();
            }
        }
        if (sendSuccess) sendSuccess = mCanSend;
        if (sendSuccess) sendSuccess =
                NetStateUtil.isNetworkConnected(CarpoolApplication.getApplication());
        m.headUrl = getSelfHeadUrl();
        m.type = MType.TYPE_RIGHT_TEXT;
        m.failInSend = !sendSuccess;
        m.otherId = mGroupId;
        m.selfId = getSelfId();
        m.text = text;
        m.date = new Date().getTime();
        return m;
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

    public List<ChatMessage> parseGson(String text) {
        Type type = new TypeToken<BaseBean<List<GroupChatBean>>>(){}.getType();
        BaseBean<List<GroupChatBean>> rootBean = mGson.fromJson(text, type);
        List<ChatMessage> msg = new ArrayList<>();

        if (rootBean.getCode() == 0) {
            List<GroupChatBean> list = rootBean.getData();
            for (GroupChatBean b : list) {
                msg.add(parseOne(b));
            }
        }
        return msg;
    }

    private ChatMessage parseOne(GroupChatBean b) {
        ChatMessage m = ChatMessage.obtain();
        m.selfId = getSelfId();
        m.date = b.getDateTime();
        m.headUrl = b.getSpeakerPhoto();
        if (b.getSpeakerId() == getSelfId()) {
            m.type = MType.TYPE_RIGHT_TEXT;
        } else {
            m.type = MType.TYPE_LEFT_TEXT;
        }
        m.text = b.getContent();
        m.otherId = mGroupId;
        return m;
    }

    @Override
    public String getCompleteUrl() {
        return new StringBuilder(BASE_GROUP_URL)
                .append("/")
                .append(mGroupId)
                .append("/")
                .append(getToken())
                .toString();
    }

}
