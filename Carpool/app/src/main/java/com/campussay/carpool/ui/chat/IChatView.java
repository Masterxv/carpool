package com.campussay.carpool.ui.chat;

import android.support.v7.app.AppCompatActivity;

import com.campussay.carpool.ui.base.IView;

/**
 * create by zuyuan on 2019/4/9
 */
public interface IChatView extends IView {
    void hideVoiceDialog();                                 //隐藏语音框
    void showVoiceDialog(boolean isFirst, boolean cancel);  //展示语音框

    void openLayoutSlideBack(boolean open); //是否关闭滑动返回

    void saveOldPosition(int position);     //滚动到指定位置
    void scrollToPosition(int position);    //滚动到底部
    void smoothToPosition(int position);    //滑动到某个位置


    void hideTopRefreshView();            //加载消息失败或者没有旧消息

    AppCompatActivity getActivity();        //获取Activity

    void setTitle(String title);            //设置标题
}
