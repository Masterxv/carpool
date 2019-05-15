package com.campussay.carpool.ui.recommend;

/**
 * creat by teng on 2019/4/6
 */
public class RecommendRoadItem {
    private String firstPlace;
    private String endPlace;
    private String time;
    private String masterPeople;

    public RecommendRoadItem(String firstPlace, String endPlace, String time, String masterPeople){
        this.firstPlace=firstPlace;
        this.endPlace=endPlace;
        this.time=time;
        this.masterPeople=masterPeople;
    }
    public String getFirstPlace() {
        return firstPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public String getTime() {
        return time;
    }

    public String getMasterPeople() {
        return masterPeople;
    }
}
