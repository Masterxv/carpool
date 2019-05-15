package com.campussay.carpool.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campussay.carpool.utils.LogUtils;

/**
 * create by WenJinG on 2019/4/5
 */
    public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IView{
        protected View mView;

        protected T mPresenter;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            mPresenter = createPresenter();
            if (mPresenter != null) {
                mPresenter.attach(this);
            }
            LogUtils.d("碎片启动了");
            mView = LayoutInflater.from(getContext()).inflate(getContentViewId(),
                    null, false);
            initView();
            getDataFromRoomAndShow();
            return mView;
        }


        @Override
        public void onDestroy() {
            if (mPresenter != null) {
                mPresenter.detach();
            }
            super.onDestroy();
        }

    //用于子fragment覆写，查询数据库中的内容更新ui。
    protected void getDataFromRoomAndShow(){
        //用法
        /*RoomHelper.getInstance().openCarpoolDatabase()
                .querySimple().observe(this, new Observer<List<TestEntity>>() {
            @Override
            public void onChanged(@Nullable List<TestEntity> testEntities) {
                for (TestEntity testEntity:testEntities){
                    //获取到数据testEntities更新ui
                }
            }
        });*/
    }

    @Override
    public void onNetworkDisconnected() {

    }

        protected abstract int getContentViewId();

        protected abstract T createPresenter();

        protected abstract void initView();
    }

