package com.campussay.carpool.ui.route.routeMember;

import com.campussay.carpool.ui.base.IView;
import com.campussay.carpool.ui.route.RouteInformationBean;

import java.util.List;

/**
 * creat by teng on 2019/4/23
 */
public interface RouteMemberList extends IView {
    void getRouteMemberList(List<RouteInformationBean.dataBean> routeInformationBeans);
    void finishEvaluate(boolean isSuccess);
    void onFinishDelete(int item);
}
