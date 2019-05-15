package com.campussay.carpool.ui.chat.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.campussay.carpool.ui.chat.manager.BaseChatManager;
import com.campussay.carpool.utils.LogUtils;

/**
 * create by zuyuan on 2019/4/22
 * 保持“心跳” 默认3s发送一次token
 */
public class KeepHeartBeat {

    private static final int WHAT = 1000;

    //默认间隔发送token为3s
    private int mIntervalTime = 10000;

    private BaseChatManager mChatManager;

    private Handler mHandler;

    private volatile boolean mRefreshState = false;

    //start方法只能够被实现一次
    private boolean isFirst = true;

    private Thread mThread  = new Thread(() -> {
        Looper.prepare();
        Looper looper = Looper.myLooper();
        mHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                if (mChatManager != null && msg.what == WHAT) {
                    if (mRefreshState) {
                        mRefreshState = false;
                    } else {
                        sendToken();
                    }

                    Message m = Message.obtain();
                    m.what = WHAT;
                    sendMessageDelayed(m, mIntervalTime);
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(WHAT, mIntervalTime);

        //轮询
        Looper.loop();
    });

    public KeepHeartBeat() {
        //保证线程的唯一性
        if(Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException(getClass().getSimpleName() + " must create in main thread!");
        }
    }

    public void register(BaseChatManager manager) {
        mChatManager = manager;
        mThread.start();
    }

    public void unRegister() {
        mChatManager = null;
        if (mThread != null) {
            mHandler.removeMessages(WHAT);
            mThread.interrupt();
        }
    }

    public void bindOnSendMessage() {
        //主动发送消息则刷新状态一次
        mRefreshState = true;
    }

    public void setIntervalTime(int time) {
        mIntervalTime = time;
    }

    private void sendToken() {
        if (mChatManager.isOpenWebSocket())
            mChatManager.sendMessageWithToken();
    }
}
