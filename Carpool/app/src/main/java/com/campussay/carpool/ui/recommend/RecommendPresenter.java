package com.campussay.carpool.ui.recommend;

import android.util.Log;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.base.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * create by WenJinG on 2019/4/6
 */
public class RecommendPresenter extends BasePresenter<RecommendFragment> {

    static String num;
    public boolean isOnRequestRecommend = false;
    
    public void postLocation() {
        if (CarpoolApplication.getAccount() != null) {
            if (mView.getLongitude() != null && mView.getLatitude() != null) {
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        "{\n" +
                                "    \"longitude\":" + mView.getLongitude() + ",\n" +
                                "    \"latitude\":" + mView.getLatitude() + "\n" +
                                "}");
                AppNetClient.getInstance()
                        .postLocation(CarpoolApplication.getAccount().getToken(), body)
                        .compose(RxSchedulers.applySchedulers())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                getPeopleOnline();
                                //recommendRoute();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("aaaa", "上传位置信息失败");
                            }

                            @Override
                            public void onComplete() {
                                Log.d("aaaa", "上传位置信息完成");
                            }
                        });
            }
        }
    }

    /**
     * 获取推荐行程
     */

    public void recommendRoute() {
        if(isOnRequestRecommend){
            return;
        }
        isOnRequestRecommend = true;
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .getRecommendList(CarpoolApplication.getAccount().getToken())
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean<RecommendDataBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean<RecommendDataBean> listBaseBean) {
                            if (listBaseBean.isSuccess()) {
                                if (listBaseBean.getData().getBriefTeamDTOS() != null) {
                                    mView.getRecommendList(listBaseBean.getData());
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            isOnRequestRecommend = false;
                            Log.d("aaaa", "推荐行程失败了");
                        }


                        @Override
                        public void onComplete() {
                            isOnRequestRecommend = false;
                            Log.d("aaaa", "完成了推荐行程");
                        }
                    });
        }
    }

    /**
     * 获取在线人数
     *
     * @return
     */
    public void getPeopleOnline() {
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .getPeopleOnLine(CarpoolApplication.getAccount().getToken())
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<RecommendPeopleNumBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RecommendPeopleNumBean listBaseBean) {

                            if (listBaseBean.isSuccess()) {

                                num = listBaseBean.getData();
                                mView.showNum(num);
                                Log.d("aaaaaa", "获取在线人数成功" + "   " + listBaseBean.getData());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("aaaa", "aaaaa失败了人数获取");
                        }


                        @Override
                        public void onComplete() {
                            Log.d("aaaa", "aaaaa完成了人数获取");
                        }
                    });
        }
    }

    /**
     * 推荐加入行程
     */
    public void recommendApplyRoute(int teamId) {
        if (CarpoolApplication.getAccount() != null) {
            AppNetClient.getInstance()
                    .recommendApplyRoute(CarpoolApplication.getAccount().getToken(), teamId)
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean listBaseBean) {
                            if (listBaseBean.isSuccess()) {
                                mView.showToast("申请成功");
                                recommendRoute();
                                Log.d("aaaa", "加入推荐行程成功了");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("aaaa", "失败了加入推荐");
                        }


                        @Override
                        public void onComplete() {
                            Log.d("aaaa", "完成了加入推荐");
                        }
                    });
        }
    }
}
