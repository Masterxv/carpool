package com.campussay.carpool.ui.chat.manager;

import com.campussay.carpool.ui.chat.other.ChatMessage;

import java.util.List;

/**
 * create by zuyuan on 2019/4/18
 */
public interface RequestMessageCallBack {
    void onReceived(List<ChatMessage> messages);
    void onFail();
}
