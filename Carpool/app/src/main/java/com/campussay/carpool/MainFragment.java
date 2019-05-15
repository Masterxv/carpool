package com.campussay.carpool;

import com.campussay.carpool.ui.base.BaseFragment;

/**
 * create by WenJinG on 2019/4/5
 */
public class MainFragment extends BaseFragment<MainPresenter> {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initView() {

    }
}
