package com.campussay.carpool.ui.chat.other;

/**
 * create by zuyuan on 2019/4/10
 */
public class ChatMessage {

    //链表
    private static final int MAX_POOL_SIZE = 500;
    private static ChatMessage msPoll;
    private static int poolSize = 0;
    private ChatMessage next;

    public MType type = MType.TYPE_LEFT_TEXT;

    //文字
    public String text = "";

    //日期
    public long date;

    //自己或者他人头像
    public String headUrl;

    //消息id
    public long msgId;

    //自己的id
    public long selfId;

    //其它的id
    public long otherId;

    //发送是否成功
    public boolean failInSend;

    public synchronized static ChatMessage obtain() {
        if (msPoll != null) {
            ChatMessage m = msPoll;
            msPoll = msPoll.next;
            m.next = null;
            poolSize--;
            return m;
        } else {
            return new ChatMessage();
        }
    }

    public synchronized void recycle() {
        if (poolSize < MAX_POOL_SIZE) {
            type = MType.TYPE_LEFT_TEXT;
            text = "";
            date = -1L;
            headUrl = "";
            failInSend = false;
            msgId = -1L;
            selfId = -1L;
            otherId = -1L;

            this.next = msPoll;
            msPoll = this;
            poolSize++;
        }
    }

}
