package com.campussay.carpool.ui.route.routeMember;

/**
 * creat by teng on 2019/4/12
 */
public class RouteInformationItem {
    private String time;
    private String firstPlace;
    private String endPlace;
    private String leader;
    private String teamName;
    public RouteInformationItem(String time, String firstPlace, String endPlace, String leader, String teamName){
        this.teamName=teamName;
        this.time=time;
        this.firstPlace=firstPlace;
        this.endPlace=endPlace;
        this.leader=leader;
    }

    public String getTime() {
        return time;
    }

    public String getFirstPlace() {
        return firstPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public String getLeader() {
        return leader;
    }

    public String getTeamName() {
        return teamName;
    }
}
