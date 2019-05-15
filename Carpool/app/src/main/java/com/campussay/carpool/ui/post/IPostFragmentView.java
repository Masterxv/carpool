package com.campussay.carpool.ui.post;

import com.campussay.carpool.ui.base.IView;

/**
 * create by WenJinG on 2019/4/18
 */
public interface IPostFragmentView extends IView {
    void showToast(String info);
    void searchItemChange();
    void onDataChange();
}
