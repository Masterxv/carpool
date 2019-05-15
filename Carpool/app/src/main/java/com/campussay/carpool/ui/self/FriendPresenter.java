package com.campussay.carpool.ui.self;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.ui.chat.ChatActivity;
import com.campussay.carpool.ui.self.testbean.FriendBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by Zhangfan on 2019/4/19
 **/
public class FriendPresenter extends BasePresenter<FriendIView> {
    private static final String TOKEN = CarpoolApplication.getAccount().getToken();
    private static final long SELF_ID = CarpoolApplication.getAccount().getId();
    private static final String HEAD_URL = CarpoolApplication.getAccount().getHeadUrl();
    private BaseBean<List<FriendBean.Data>> list = new BaseBean<>();

    public void getFriendsList(){
        if (TOKEN != null) {
            AppNetClient.getInstance()
                    .getSelfFriendsList(TOKEN)
                    .compose(RxSchedulers.applySchedulers())
                    .subscribe(new Observer<BaseBean<List<FriendBean.Data>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean<List<FriendBean.Data>> listBaseBean) {
                            if (listBaseBean.isSuccess() && listBaseBean.getData() != null) {
                                mView.getFriendsList(listBaseBean.getData());
                                list = listBaseBean;
                            }
                            if (list.isSuccess()){
                                mView.isRefreshSuccess("刷新成功");
                            }else{
                                mView.isRefreshSuccess("刷新失败");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.isRefreshSuccess("刷新失败");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public View.OnClickListener getOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriendsList();
            }
        };
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriendsList();
            }
        };
    }

    public OnItemButtonClickListener getItemButtonListener(Activity context){
        return new OnItemButtonClickListener() {
            @Override
            public void clickButton(int position) {
                if (TOKEN != null && SELF_ID != 0 && HEAD_URL != null) {
                    ChatActivity.startSelfChatting(list.getData().get(position).getNickName(),
                            context, TOKEN, SELF_ID,
                            Long.parseLong(list.getData().get(position).getId()),
                            HEAD_URL, list.getData().get(position).getHeadUrl());
                }else{
                    Toast.makeText(context, "token等参数未知", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
