package com.campussay.carpool.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.self.testbean.FriendBean;
import com.campussay.carpool.wxapi.LoginBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * create by zuyuan on 2019/4/23
 */
public class LoginUtil {

    private static final String FILE_NAME = "a3d3r4dwe4rwefag43";

    private static final String KEY = "my_self_token";

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String HEAD_URL = "headUrl";
    private static final String GENDER = "gender";

    private static boolean isRunning = false;

    public static void login(Activity activity, CallBack callBack) {
        isRunning = true;
        SharedPreferences p = activity.getSharedPreferences(FILE_NAME,
                Activity.MODE_PRIVATE);
        String token = p.getString(KEY, "");
        if (token.isEmpty()) {
            callBack.onError(CallBack.FIRST);
            isRunning = false;
            return;
        }

        if (!NetStateUtil.isNetworkConnected(CarpoolApplication.getApplication())) {
            callBack.onError(CallBack.INTERNET_ERROR);
            isRunning = false;
            return;
        }

        String name = p.getString(NAME, "");
        String headUrl = p.getString(HEAD_URL, "");
        int sex = p.getInt(GENDER, 0);
        int id = p.getInt(ID, 0);

        AppNetClient.getInstance()
                .getSelfFriendsList(token)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<BaseBean<List<FriendBean.Data>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<FriendBean.Data>> b) {
                        isRunning = false;
                        if (b.isSuccess()) {
                            CarpoolApplication.setAccount(new CarpoolApplication.Account(
                                    id, token, name, headUrl, sex));
                            callBack.onSuccess();
                        } else {
                            if (b.getCode() != 1) {   //身份过期
                                callBack.onError(CallBack.OVERDUE);
                            } else{
                                callBack.onError(CallBack.ERROR);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(CallBack.ERROR);
                        isRunning = false;
                    }

                    @Override
                    public void onComplete() {
                        isRunning = false;
                    }
                });
    }

    public static void save(Activity activity, LoginBean bean) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(FILE_NAME,
                Activity.MODE_PRIVATE).edit();
        CarpoolApplication.setAccount(new CarpoolApplication.Account(
                Integer.valueOf(bean.getData().getId()),
                bean.getData().getToken(),
                bean.getData().getNickName(),
                bean.getData().getHeadUrl(),
                bean.getData().getSex()
        ));
        editor.putString(KEY, bean.getData().getToken());
        editor.putString(NAME, bean.getData().getNickName());
        editor.putInt(ID, Integer.valueOf(bean.getData().getId()));
        editor.putString(HEAD_URL, bean.getData().getHeadUrl());
        editor.putInt(GENDER, bean.getData().getSex());
        editor.apply();
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public interface CallBack {
        void onSuccess();
        void onError(int code);

        int INTERNET_ERROR = 1001;
        int OVERDUE = 1002;
        int ERROR = 1003;
        int FIRST = 1004;
    }


}
