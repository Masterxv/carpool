package com.campussay.carpool.ui.post;

/**
 * create by WenJinG on 2019/4/7
 */
public class PostRecyclerViewItem {
    private String postRouteStart;
    private String postRouteEnd;
    private String postTime;
    private String userName;
    private String teamId;

    public PostRecyclerViewItem(String postRoute,String postRouteEnd,String postTime,String userName,String teamId){
        this.postRouteStart = postRoute;
        this.postRouteEnd = postRouteEnd;
        this.postTime = postTime;
        this.userName = userName;
        this.teamId = teamId;

    }

    public String getPostRouteStart() {
        return postRouteStart;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostRouteEnd() {
        return postRouteEnd;
    }

    public String getTeamId() {
        return teamId;
    }
}
