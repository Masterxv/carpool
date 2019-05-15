package com.campussay.carpool.ui.self;

import com.campussay.carpool.ui.base.IView;
import com.campussay.carpool.ui.self.testbean.FriendBean;

import java.util.List;

/**
 * Create by Zhangfan on 2019/4/19
 **/
public interface FriendIView extends IView {
    void getFriendsList(List<FriendBean.Data> list);
    void isRefreshSuccess(String isSuccess);
}
