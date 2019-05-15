package com.campussay.carpool.ui.base;

/**
 * create by WenJinG on 2019/4/4
 */
public class BasePresenter<T extends IView> {
    protected T mView;

    public void attach (T view){   // 与v层建立联系
        this.mView = view;
    }

    public void detach(){      // 与v层断开联系
        mView = null;
    }
}
