package com.campussay.carpool.ui.route.RouteLeader;

/**
 * creat by teng on 2019/4/11
 */
public class RouteLeaderApplyItem {
    private int imageId;
    private String otherApplyName;
    private String isChoose;
    public RouteLeaderApplyItem(int imageId, String otherApplyName, String isChoose){
        this.imageId=imageId;
        this.otherApplyName=otherApplyName;
        this.isChoose=isChoose;
    }

    public int getImageId() {
        return imageId;
    }

    public String getOtherApplyName() {
        return otherApplyName;
    }

    public String getIsChoose() {
        return isChoose;
    }
}
