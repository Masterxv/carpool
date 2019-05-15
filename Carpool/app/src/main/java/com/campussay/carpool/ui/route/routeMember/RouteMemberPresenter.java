package com.campussay.carpool.ui.route.routeMember;

import android.util.Log;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * creat by teng on 2019/4/16
 */
public class RouteMemberPresenter extends BasePresenter<RouteMemberFargment> {

    private boolean isOnRequest = false;

    public void getRouteMemberList() {
        if (CarpoolApplication.getAccount() != null && !isOnRequest) {
            isOnRequest = true;
            AppNetClient.getInstance()
                    .getRouteList(CarpoolApplication.getAccount().getToken())
                    .compose(RxSchedulers.applySchedulers())
                    .compose(((RxAppCompatActivity) (mView.getActivity())).bindUntilEvent(ActivityEvent.STOP))
                    .subscribe(new Observer<BaseBean<List<RouteInformationBean.dataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean<List<RouteInformationBean.dataBean>> listBaseBean) {
                            if (listBaseBean.isSuccess()) {
                                mView.getRouteMemberList(listBaseBean.getData());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            isOnRequest = false;
                            Log.d("aaaa", "member big失败了");
                        }

                        @Override
                        public void onComplete() {
                            isOnRequest = false;
                            Log.d("aaaa", "member big完成了");
                        }
                    });

        }
    }

    /**
     * 删除行程
     */
    public void deleteRoute(int position, int teamId) {
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .cancelRoute(CarpoolApplication.getAccount().getToken(), new Long((long) teamId))
                    .compose(RxSchedulers.applySchedulers())
                    .compose(((RxAppCompatActivity) (mView.getActivity())).bindUntilEvent(ActivityEvent.STOP))
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(BaseBean listBaseBean) {
                            if (listBaseBean.isSuccess()) {
                                Toast.makeText(CarpoolApplication.getApplication(),
                                        "删除成功", Toast.LENGTH_SHORT).show();
                                mView.onFinishDelete(position);
                            } else {
                                Toast.makeText(CarpoolApplication.getApplication(),
                                        "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(CarpoolApplication.getApplication(),
                                    "删除失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            Log.d("aaaa", "member删除行程完成了");
                        }
                    });
        }
    }

    public void leaderRouteOver(int teamId, int evaluate) {
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .routeOver(CarpoolApplication.getAccount().getToken(), new Integer(teamId), evaluate)
                    .compose(RxSchedulers.applySchedulers())
                    .compose(((RxAppCompatActivity) (mView.getActivity())).bindUntilEvent(ActivityEvent.STOP))
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean listBaseBean) {

                            mView.finishEvaluate(listBaseBean.isSuccess());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("aaaa", "member评价行程失败了");
                            mView.finishEvaluate(false);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("aaaa", "member评价行程完成了");
                        }
                    });
        }
    }
}
