package com.campussay.carpool.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.campussay.carpool.ui.adapter.ChatRecycleAdapter;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.ui.chat.manager.BaseChatManager;
import com.campussay.carpool.ui.chat.manager.GroupChatManager;
import com.campussay.carpool.ui.chat.other.MType;
import com.campussay.carpool.ui.chat.manager.MessageListener;
import com.campussay.carpool.ui.chat.manager.PlaceChatManager;
import com.campussay.carpool.ui.chat.manager.RequestMessageCallBack;
import com.campussay.carpool.ui.chat.manager.SelfChatManager;
import com.campussay.carpool.ui.chat.other.ChatMessage;
import com.campussay.carpool.ui.chat.widget.ChatFrameLayout;
import com.campussay.carpool.utils.ScreenAttrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zuyuan on 2019/4/9
 */
public class ChatPresenter extends BasePresenter<IChatView> {

    //是否是第一次进入
    private boolean isFirst = true;

    //记录的屏幕高度
    private int mRecordHeight = 0;

    //聊天RecyclerView的Adapter
    private ChatRecycleAdapter mAdapter;

    //软件盘是否正在展示
    public boolean mkeyboardShow = false;

    //锁住一次请求，防止同时发起多次请求
    private boolean onFindOldMessage = false;

    //聊天管理器
    private BaseChatManager mChatManager;

    //防止多次弹出连接失败的提示
    private boolean isConnect = true;

    Handler mUiHandler = new Handler(Looper.getMainLooper());

    //触发滑动返回的距离
    public static final float SLIDE_CANCEL_DISTANCE = 120f * ScreenAttrUtil.getDensity();

    //消息接收者
    private MessageListener mListener = new MessageListener() {
        @Override
        public void receivedMsg(List<ChatMessage> msgs) {
            if (!isConnect) isConnect = true;
            //添加时间消息 其它两个Mananger每一次回调的时候都会返回历史消息
            if (!(mChatManager instanceof SelfChatManager)) mAdapter.recycleMsg();
            mAdapter.addMessage(getMessagesWithTimeMessage(msgs));
            mAdapter.notifyDataSetChanged();
            if (mView != null)
                mView.scrollToPosition(mAdapter.getListSize());
        }

        @Override
        public void receivedMsg(ChatMessage msg) {
            if (!isConnect) isConnect = true;
            //添加时间消息
            long beforeOne = 0L;
            if (mAdapter.getListSize() != 0) beforeOne =
                    mAdapter.getMsgs().get(mAdapter.getListSize() - 1).date;
            if (msg.date - beforeOne >= 180000) {
                ChatMessage timeM = ChatMessage.obtain();
                timeM.type = MType.TYPE_TIME;
                timeM.date = msg.date;
                timeM.selfId = msg.selfId;
                timeM.otherId = msg.otherId;
                mAdapter.addMessage(timeM);
            }
            mAdapter.addMessage(msg);
            mAdapter.notifyDataSetChanged();
            if (mView != null)
                mView.smoothToPosition(mAdapter.getListSize());
        }

        @Override
        public void fail() {
            if (mView != null && isConnect) {
                Toast.makeText(mView.getActivity(), "连接失败，请稍后重试",
                        Toast.LENGTH_SHORT).show();
                mView.hideTopRefreshView();
            }

            if (isConnect) isConnect = false;
        }
    };

    @Override
    public void detach() {
        super.detach();
        //保存记录
        mChatManager.refreshDataBaseWithRecycleMsg(mAdapter.getMsgs());
        mChatManager.detach();
    }

    /**
     * 语音按键的上滑取消逻辑、触发发送语音逻辑
     */
    public View.OnTouchListener createVoiceBtnListener() {
            return new View.OnTouchListener() {
                float startY = 0f;
                boolean nowIsCancel = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mView.openLayoutSlideBack(false);
                            nowIsCancel = false;
                            mView.showVoiceDialog(true, false);
                            startY = event.getY();
                            startVoice();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (startY - event.getY() > SLIDE_CANCEL_DISTANCE && !nowIsCancel) {
                                nowIsCancel = true;
                                mView.showVoiceDialog(false, true);
                            } else if (startY - event.getY() < SLIDE_CANCEL_DISTANCE && nowIsCancel) {
                                nowIsCancel = false;
                                mView.showVoiceDialog(false, false);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            mView.hideVoiceDialog();
                            if (!nowIsCancel) {
                                sendVoice();
                            } else {
                                cancelVoice();
                            }
                            mView.openLayoutSlideBack(true);
                            break;
                        default:
                            mView.openLayoutSlideBack(true);
                            mView.hideVoiceDialog();
                            cancelVoice();
                            break;
                    }
                    return true;
                }
            };
        }

    /**
     * 消息列表的布局绘制完成监听
     */
    public ViewTreeObserver.OnGlobalLayoutListener createRecyclerGlobalListener() {
        return () -> {
            if (isFirst) {
                findOldMessageByLocal();
                isFirst = false;
            }

        };
    }

    /**
     * 软键盘弹出监听
     */
    public ViewTreeObserver.OnGlobalLayoutListener createRootGlobalListener(View rootView) {
        return () -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);

            int visibleHeight = r.height();
            if (mRecordHeight == 0) {
                mRecordHeight = visibleHeight;
            } else if (mRecordHeight - visibleHeight > 200) {
                //软键盘弹出
                if (!mkeyboardShow && mView != null) {
                    mkeyboardShow = true;
                    //列表滑动到底部
                    mView.scrollToPosition(mAdapter.getListSize());
                }
            } else if (mRecordHeight == visibleHeight) {
                //软键盘收回
                mkeyboardShow = false;
            }
        };
    }

    /**
     * 创建Adapter
     */
    public ChatRecycleAdapter createRecyclerAdapter() {
        if (mAdapter == null) {
            mAdapter = new ChatRecycleAdapter(new ArrayList<>());
        }
        return mAdapter;
    }

    /**
     * 创建刷新监听
     */
    public ChatFrameLayout.OnRefreshListener createRefreshListener() {
        return this::findOldMessageByNet;
    }

    public ChatRecycleAdapter getRecyclerAdapter() {
        return mAdapter;
    }

    /**
     * 解析Intent信息
     */
    public void parseIntentExtra(Intent i) {
        int t = i.getIntExtra("type", 0);
        long selfId = i.getLongExtra("selfId", 0);
        long otherId = i.getLongExtra("otherId", 0);
        String token = i.getStringExtra("token");
        String selfHeadUrl = i.getStringExtra("selfHeadUrl");
        String title = i.getStringExtra("title");
        String otherHeadUrl = i.getStringExtra("otherHeadUrl");
        mView.setTitle(title);

        switch (ChatActivity.ChatType.get(t)) {
            case SELF_CHAT:
                mChatManager = new SelfChatManager(token, selfId, otherId,
                        selfHeadUrl, otherHeadUrl, mListener);
                break;
            case GROUP_CHAT:
                mChatManager = new GroupChatManager(token, selfId, otherId, selfHeadUrl, mListener);
                break;
            case PLACE_CHAT:
                mChatManager = new PlaceChatManager(token, selfId, selfHeadUrl,  mListener);
                break;
            default:
                throw new RuntimeException("not found correct type, " +
                        "you should use ChatActivity.startxxx()!");
        }
    }

    /**
     * 活动停止时保存记录
     */
    public void onActivityStop() {
        mChatManager.closeSocket();
        //主动置为false，防止报出连接错误提示
        isConnect = false;
    }

    public void onActivityResume() {
        if (!isFirst) mChatManager.openSocket();
    }

    /**
     * 查找本地消息记录
     */
    private void findOldMessageByLocal() {
        if (onFindOldMessage) return;

        onFindOldMessage = true;
        mChatManager.findOldMessagesByDataBase(new RequestMessageCallBack() {
            @Override
            public void onReceived(List<ChatMessage> messages) {
                if (mView == null) return;
                mAdapter.addMessage(messages);
                mAdapter.notifyDataSetChanged();
                mView.scrollToPosition(messages.size());
                mView.hideTopRefreshView();
                onFindOldMessage = false;
                mChatManager.openSocket();
            }

            @Override
            public void onFail() {
                mView.hideTopRefreshView();
                onFindOldMessage = false;
                mChatManager.openSocket();
            }
        });
    }

    /**
     * 查找网络消息记录
     */
    private void findOldMessageByNet() {
        if (onFindOldMessage) return;

        onFindOldMessage = true;
        long id = 0;
        final int listSize = mAdapter.getListSize();
        final List<ChatMessage> list = mAdapter.getMsgs();

        //防止时间戳影响
        for (int i = 0; i < listSize; i++) {
            MType type = list.get(i).type;
            if (type == MType.TYPE_LEFT_TEXT || type == MType.TYPE_RIGHT_TEXT ) {
                id = list.get(i).msgId;
                break;
            }
        }
        mChatManager.findOldMessagesByNet(id, new RequestMessageCallBack() {
            @Override
            public void onReceived(List<ChatMessage> messages) {
                if (mView == null) return;

                if (messages.size() == 0) {
                    onFail();
                } else {
                    //添加时间戳
                    List<ChatMessage> listWithTime = getMessagesWithTimeMessage(messages);
                    mAdapter.addBeforeMessage(listWithTime);
                    //不是第一次加载则保持原位置
                    int p = listWithTime.size() == mAdapter.getListSize() ? listWithTime.size()
                            : (listWithTime.size() + 1);
                    //偏移量在Activity中设置的，此处设置无效
                    mView.saveOldPosition(p);
                    mView.hideTopRefreshView();
                    onFindOldMessage = false;
                }
            }

            @Override
            public void onFail() {
                mUiHandler.postDelayed(() -> {
                    if (mView != null)
                        mView.hideTopRefreshView();
                }, 500);
                onFindOldMessage = false;
            }
        });
    }

    /**
     * 发送消息
     */
    public void sendMessage(String str) {
        ChatMessage m = mChatManager.sendMessage(str);
        if (m == null) return;
        mListener.receivedMsg(m);
    }

    /**
     * 为消息列表生成时间消息
     */
    private List<ChatMessage> getMessagesWithTimeMessage(List<ChatMessage> list) {
        if (list.size() == 0) return list;

        long beforeOne;
        if (mAdapter.getListSize() != 0) {
            beforeOne = mAdapter.getMsgs().get(mAdapter.getListSize() - 1).date;
        } else {
            beforeOne = 0L;
        }

        List<ChatMessage> listWithTime = new ArrayList<>();
        final int lastIndex = list.size() - 1;
        for (int i = 0; i <= lastIndex; i++) {
            //生成时间戳
            ChatMessage old = list.get(i);
            //大于3分钟则添加时间戳
            if (old.date - beforeOne >= 180000) {
                ChatMessage m = ChatMessage.obtain();
                m.selfId = old.selfId;
                m.otherId = old.otherId;
                m.date = old.date;
                m.type = MType.TYPE_TIME;
                listWithTime.add(m);
                listWithTime.add(old);
            } else {
                listWithTime.add(old);
            }
            beforeOne = old.date;
        }

        return listWithTime;
    }

    /**
     * 语音消息
     */
    private void sendVoice() { }
    private void cancelVoice() { }
    private void startVoice() { }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(AppCompatActivity activity) {
        if (!mkeyboardShow) return;

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }
}
