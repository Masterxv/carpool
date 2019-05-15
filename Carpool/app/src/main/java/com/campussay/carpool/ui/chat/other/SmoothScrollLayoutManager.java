package com.campussay.carpool.ui.chat.other;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * create by zuyuan on 2019/4/15
 * 设置自定义滑动速度的布局管理器
 */
public class SmoothScrollLayoutManager extends LinearLayoutManager {

    public SmoothScrollLayoutManager(Context context) {
        super(context);
    }

    public static final float SCROLL_TIME_1PX = 150f;

    private float mScrollTime_1PX = 150f;

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state,
                                       int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(
                recyclerView.getContext()) {
                    // 返回：滑过1px时经历的时间(ms)。
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return mScrollTime_1PX / displayMetrics.densityDpi;
                    }
                };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    public void setSpeedMultiple(float m) {
        mScrollTime_1PX = m * SCROLL_TIME_1PX;
    }
}
