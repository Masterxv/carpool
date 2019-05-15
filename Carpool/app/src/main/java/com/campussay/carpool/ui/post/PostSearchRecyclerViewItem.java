package com.campussay.carpool.ui.post;

/**
 * create by WenJinG on 2019/4/13
 */
public class PostSearchRecyclerViewItem {
    private String location;
    private String detailAddress;

    public PostSearchRecyclerViewItem(String location,String detailAddress){
        this.location = location;
        this.detailAddress = detailAddress;
    }
    public String getLocation() {
        return location;
    }

    public String getDetailAddress() {
        return detailAddress;
    }
}
