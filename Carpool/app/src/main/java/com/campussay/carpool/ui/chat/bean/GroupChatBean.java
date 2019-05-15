package com.campussay.carpool.ui.chat.bean;

/**
 * create by zuyuan on 2019/4/18
 */
public class GroupChatBean {
    /**
     * content : 你好
     * dateTime : 1555941038000
     * messageId : 1031
     * speakerId : 109
     * speakerName : 勿忘
     * speakerPhoto : http://thirdwx.qlogo.cn/mmopen/vi_32/Xf437UjemORVyBT7QhicabexsXM6TdicPWSsbZfM95BibhLMVOjTkVtNWO6lOpsf1SfL7vVXutG1HAOrH1lHePxGQ/132
     * teamId : 428
     */

    private String content;
    private long dateTime;
    private int messageId;
    private int speakerId;
    private String speakerName;
    private String speakerPhoto;
    private int teamId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(int speakerId) {
        this.speakerId = speakerId;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerPhoto() {
        return speakerPhoto;
    }

    public void setSpeakerPhoto(String speakerPhoto) {
        this.speakerPhoto = speakerPhoto;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
