package com.campussay.carpool.ui.chat.bean;

/**
 * create by zuyuan on 2019/4/17
 */
public class SelfChatBean {

    /**
     * content : test
     * create : 1555129110000
     * own : false
     * messageId : 本条记录id
     */

    private String content;
    private long create;
    private boolean own;
    private long messageId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
