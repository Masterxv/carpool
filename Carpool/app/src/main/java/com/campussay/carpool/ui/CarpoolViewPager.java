package com.campussay.carpool.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * create by WenJinG on 2019/4/6
 */
public class CarpoolViewPager extends ViewPager {

    private boolean  isCanScroll = true;

    public CarpoolViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CarpoolViewPager(Context context) {
        super(context);
    }


    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);

    }
}
