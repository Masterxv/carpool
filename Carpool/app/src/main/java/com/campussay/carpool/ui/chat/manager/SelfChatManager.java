package com.campussay.carpool.ui.chat.manager;

import android.support.annotation.Nullable;
import android.util.Log;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.room.RoomHelper;
import com.campussay.carpool.ui.chat.bean.SelfChatBean;
import com.campussay.carpool.ui.chat.other.ChatMessage;
import com.campussay.carpool.ui.chat.other.MType;
import com.campussay.carpool.ui.chat.sql.GroupAndSelfChatEntity;
import com.campussay.carpool.utils.LogUtils;
import com.campussay.carpool.utils.NetStateUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * create by zuyuan on 2019/4/16
 * 私聊
 */
public class SelfChatManager extends BaseChatManager {

    public static final String BASE_SELF_URL = "ws://pin.varbee.com/chatRoom/private/chat";

    private String mFriendHeadUrl;

    private long mFriendId;

    private volatile boolean mCanSend = false;

    public SelfChatManager( String token, long selfId, long friendId,
                            String selfHeadUrl, String friendHeadUrl,
                            MessageListener callBack) {
        super(token, selfId, selfHeadUrl, callBack);
        setMessageListener(callBack);
        mFriendId = friendId;
        mFriendHeadUrl = friendHeadUrl;
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
        boolean sendSuccess = false;
        if (mWebSocket == null) {
            initWebSocket();
        } else {
            if (!(sendSuccess = mWebSocket.send(text))) {
                mWebSocket.cancel();
                initWebSocket();
            }
        }
        if (sendSuccess) sendSuccess = mCanSend;
        //没得网络则发送失败
        if (sendSuccess) sendSuccess =
                NetStateUtil.isNetworkConnected(CarpoolApplication.getApplication());
        m.headUrl = getSelfHeadUrl();
        m.type = MType.TYPE_RIGHT_TEXT;
        m.failInSend = !sendSuccess;
        m.msgId = 0;
        m.selfId = getSelfId();
        m.otherId = mFriendId;
        m.text = text;
        m.date = new Date().getTime();
        return m;
    }

    @Override
    public void findOldMessagesByNet(long msgId, RequestMessageCallBack callBack) {
        AppNetClient.getInstance()
                .selfChatRecord(getToken(), mFriendId, 20, msgId)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(
                        bean -> callBack.onReceived(parseReceivedBean(bean)),
                        t -> callBack.onFail());
    }

    @Override
    public void findOldMessagesByDataBase(RequestMessageCallBack callBack) {
        mExecutor.execute(() -> {
            GroupAndSelfChatEntity[] entities = RoomHelper.getInstance()
                    .openCarpoolDatabase()
                    .getDatabase()
                    .getChatRecordDao()
                    .querySelfChatRecord(getSelfId(), mFriendId);

            List<ChatMessage> messages = new ArrayList<>();
            for (GroupAndSelfChatEntity e : entities) {
                ChatMessage m = ChatMessage.obtain();
                m.selfId = e.getAccount_id();
                m.otherId = e.getFriend_id();
                m.msgId = e.getMsg_id();
                m.headUrl = e.getHead_url();
                m.date = e.getDate();
                m.type = MType.get(e.getType());
                m.failInSend = e.isSend_fail();
                m.text = e.getContent();
                messages.add(m);
            }
            mUiHandler.post(() -> callBack.onReceived(messages));
        });
    }

    @Override
    public void refreshDataBaseWithRecycleMsg(List<ChatMessage> messages) {
        final int listSize = messages.size();
        if (listSize == 0) return;
        List<ChatMessage> recordList;
        int startIndex;

        //从第25条消息开始检索，直到检索到左边消息或者包含准确Id的消息
        if(listSize >= 25) {
            for (startIndex = listSize - 24; startIndex < listSize; startIndex++) {
                if (messages.get(startIndex).type == MType.TYPE_LEFT_TEXT
                        || messages.get(startIndex).msgId != 0) break;
            }
        } else {
            for (startIndex = 0 ; startIndex < listSize; startIndex++) {
                if (messages.get(startIndex).type == MType.TYPE_LEFT_TEXT
                        || messages.get(startIndex).msgId != 0) break;
            }
        }
        recordList = messages.subList(startIndex, listSize);
        if (recordList.size() == 0) return;

        mExecutor.execute(() -> {
            RoomHelper.getInstance()
                    .openCarpoolDatabase()
                    .getDatabase()
                    .getChatRecordDao()
                    .deleteSelfChatBeforeRecord(getSelfId(), mFriendId);

            GroupAndSelfChatEntity[] entities = new GroupAndSelfChatEntity[recordList.size()];
            int i = 0;
            for (ChatMessage m : recordList) {
                GroupAndSelfChatEntity entity = new GroupAndSelfChatEntity();
                entity.setAccount_id(getSelfId());
                entity.setFriend_id(mFriendId);
                entity.setContent(m.text);
                entity.setDate(m.date);
                entity.setHead_url(m.headUrl);
                entity.setMsg_id(m.msgId);
                entity.setSend_fail(m.failInSend);
                entity.setType(m.type.intValue);
                entities[i] = entity;
                i++;
            }
            RoomHelper.getInstance()
                    .openCarpoolDatabase()
                    .getDatabase()
                    .getChatRecordDao()
                    .insertSelfChatRecord(entities);


            //释放消息
            for (ChatMessage m: messages) {
                m.recycle();
            }
        });
    }

    private List<ChatMessage> parseGson(String gson) {
        Type type = new TypeToken<BaseBean<List<SelfChatBean>>>(){}.getType();
        BaseBean<List<SelfChatBean>> rootBean = mGson.fromJson(gson, type);
        return parseReceivedBean(rootBean);
    }

    private List<ChatMessage> parseReceivedBean(BaseBean<List<SelfChatBean>> rootBean) {
        List<ChatMessage> ms = new ArrayList<>();
        if (rootBean.getCode() == 0) {
            List<SelfChatBean> beans = rootBean.getData();
            if (beans == null) return ms;
            int index = beans.size() - 1;
            for (; index >= 0; index--) {
                SelfChatBean b = beans.get(index);
                ChatMessage m = ChatMessage.obtain();
                m.date = b.getCreate();
                m.text = b.getContent();
                m.otherId = mFriendId;
                m.selfId = getSelfId();
                m.type = b.isOwn() ? MType.TYPE_RIGHT_TEXT : MType.TYPE_LEFT_TEXT;
                m.msgId = b.getMessageId();
                m.headUrl = b.isOwn() ? getSelfHeadUrl() : mFriendHeadUrl;
                ms.add(m);
            }
        }

        return ms;
    }

    @Override
    public String getCompleteUrl() {
        return new StringBuilder(BASE_SELF_URL)
                .append("/")
                .append(mFriendId)
                .append("/")
                .append(getToken())
                .toString();
    }
}
