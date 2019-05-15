package com.campussay.carpool.ui.chat.manager;

import com.campussay.carpool.ui.chat.other.ChatMessage;

import java.util.List;

public interface MessageListener {
        void receivedMsg(List<ChatMessage> msgs);
        void receivedMsg(ChatMessage msg);
        void fail();
}