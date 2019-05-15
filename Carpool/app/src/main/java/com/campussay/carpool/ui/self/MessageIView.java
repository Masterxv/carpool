package com.campussay.carpool.ui.self;

import com.campussay.carpool.ui.base.IView;
import com.campussay.carpool.ui.self.testbean.MessagesBean;

import java.util.List;

/**
 * Create by Zhangfan on 2019/4/19
 **/
public interface MessageIView extends IView {
    void getMessageList(List<MessagesBean.Data> list);
    void isRefreshSuccess(String isSuccess);
}
