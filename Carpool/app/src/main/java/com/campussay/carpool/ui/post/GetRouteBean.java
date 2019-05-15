package com.campussay.carpool.ui.post;

/**
 * create by WenJinG on 2019/4/17
 */
public class GetRouteBean {


    /**
     * id : 123
     * leaderName : 李彬楷
     * originName : 测试起点3
     * destinationName : 测试终点3
     * targetTime : 2019-04-07T15:39:17
     */

    private String id;
    private String leaderName;
    private String originName;
    private String destinationName;
    private String targetTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(String targetTime) {
        this.targetTime = targetTime;
    }
}
