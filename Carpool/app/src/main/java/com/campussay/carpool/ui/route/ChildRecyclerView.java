package com.campussay.carpool.ui.route;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * creat by teng on 2019/4/13
 */
public class ChildRecyclerView extends RecyclerView {

    public ChildRecyclerView(Context context) {
        super(context);
    }

    public ChildRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN && mListener != null) {
            //告诉父RecyclerView：当前点击的对象是我
            mListener.onDown(this);
        }
        return super.onInterceptTouchEvent(e);
    }

    private OnDownListener mListener;

    public void setOnDownListener(OnDownListener listener) {
        mListener = listener;
    }

    public interface OnDownListener {
        void onDown(RecyclerView view);
    }
}
