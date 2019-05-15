package com.campussay.carpool.ui.route.RouteLeader;

import com.campussay.carpool.ui.base.IView;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.ui.route.routeMember.RouteInformationItem;

import java.util.List;

/**
 * creat by teng on 2019/4/23
 */
public interface RouteLeaderBigList extends IView {
    void getRouteLeaderBigList(List<RouteInformationBean.dataBean> routeInformationBean);
    void initDelete(int position);
    void finishEvaluate(int position, boolean isSuccess);
    void finishAgreeJoin(boolean isSuccess, int itemPosition);
}
