package com.campussay.carpool.ui.chat.bean;

/**
 * create by zuyuan on 2019/4/18
 */
public class PlaceChatBean {


    /**
     * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Xf437UjemORVyBT7...
     * content : 你好
     * create : 1555933227531
     * nickName : 勿忘
     * own : true
     * time : 2019-04-22T19:40:27.531
     */

    private String avatar;
    private String content;
    private long create;
    private String nickName;
    private boolean own;
    private String time;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
