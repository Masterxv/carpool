package com.campussay.carpool.ui.route.routeApply;

import android.util.Log;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.ui.route.bean.ApplyBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Create by Zhangfan on 2019/4/20
 **/
public class ApplyPresenter extends BasePresenter<RouteApplyFragment> {

    public void getApplyList(@NonNull int page, boolean isMorePage){
        if(CarpoolApplication.getAccount() != null){
        AppNetClient.getInstance()
                .getApplyList(CarpoolApplication.getAccount().getToken(),page)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<BaseBean<List<ApplyBean.Data>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<ApplyBean.Data>> listBaseBean) {
                        if (listBaseBean.isSuccess()) {
                            mView.getApplyList(listBaseBean.getData(), isMorePage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    }
}
