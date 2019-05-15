package com.campussay.carpool.ui.chat.sql;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * create by zuyuan on 2019/4/16
 */
@Entity(tableName = "self_chat_table")
public class GroupAndSelfChatEntity {

    @PrimaryKey(autoGenerate = true)
    private int key_id;
    private long account_id;
    private long friend_id;     //还可作为团队Id来使用
    private long msg_id;
    private String content;
    private long date;
    private String head_url;
    private int type;
    private boolean send_fail;

    public String getHead_url() {
        return head_url;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

    public int getKey_id() {
            return key_id;
        }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(long friend_id) {
        this.friend_id = friend_id;
    }

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSend_fail() {
        return send_fail;
    }

    public void setSend_fail(boolean send_fail) {
        this.send_fail = send_fail;
    }
}
