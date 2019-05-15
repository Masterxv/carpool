package com.campussay.carpool.ui.route.RouteLeader;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ShellActivity;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.ui.recommend.RecommendDataBean;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.ui.route.routeMember.RouteInformationItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * creat by teng on 2019/4/16
 */
public class RouteLeaderPresenter extends BasePresenter<RouteLeaderFragment> {

    //加锁，不能进行多次请求
    private volatile boolean isOnRequest = false;

    public void getRouteLeaaderBigList() {
        if (isOnRequest) return;

        isOnRequest = true;
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .getRouteList(CarpoolApplication.getAccount().getToken())
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean<List<RouteInformationBean.dataBean>>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean<List<RouteInformationBean.dataBean>> listBaseBean) {


                            Log.d("aaaaa", "leader big行程获取成功" + listBaseBean.isSuccess());


                            if (listBaseBean.isSuccess()) {
                                mView.getRouteLeaderBigList(listBaseBean.getData());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("aaaa", "leader  big行程失败了");
                            isOnRequest = false;
                        }

                        @Override
                        public void onComplete() {
                            Log.d("TAG", "leader  big行程完成了");
                            isOnRequest = false;
                        }
                    });
        }
    }

    /**
     * 删除行程
     */
    public void deleteRoute(int teamId, int position) {
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .cancelRoute(CarpoolApplication.getAccount().getToken(), new Long((long) teamId))
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(BaseBean listBaseBean) {
                            if (listBaseBean.isSuccess()) {
                                mView.initDelete(position);
                                Toast.makeText(CarpoolApplication.getApplication(),
                                        "删除成功", Toast.LENGTH_SHORT).show();
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
                            Log.d("aaaa", "取消行程完成了");
                        }
                    });
        }
    }


    /**
     * 同意申请
     */
    public void applyRoute(int teamId, int userId, int itemPosition) {
        if (CarpoolApplication.getAccount() != null) {
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    "{\n" +
                            "    \"teamId\":" + teamId + ",\n" +
                            "    \"userId\":" + userId + "\n" +
                            "}");
            AppNetClient.getInstance()
                    .applyRoute(CarpoolApplication.getAccount().getToken(), body)
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean bean) {
                                mView.finishAgreeJoin(bean.isSuccess(), itemPosition);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.finishAgreeJoin(false, itemPosition);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    //行程完结评价
    public void leaderRouteOver(int position, int teamId, int evaluate) {
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .routeOver(CarpoolApplication.getAccount().getToken(), new Integer(teamId), evaluate)
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean listBaseBean) {
                            mView.finishEvaluate(position, listBaseBean.isSuccess());
                        }


                        @Override
                        public void onError(Throwable e) {
                            mView.finishEvaluate(position,false);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
