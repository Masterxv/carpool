package com.campussay.carpool.ui.route.routeApply;

import com.campussay.carpool.ui.base.IView;
import com.campussay.carpool.ui.route.bean.ApplyBean;

import java.util.List;

/**
 * Create by Zhangfan on 2019/4/20
 **/
public interface ApplyIVew extends IView {
    void getApplyList(List<ApplyBean.Data> list, boolean isMorePage);
}
