package com.campussay.carpool.ui.chat.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * create by zuyuan on 2019/4/11
 * 实现: 加了一个横向滑动指定距离触发CloseListener的功能
 */
public class ChatContentLayout extends ConstraintLayout {

    private VelocityTracker mTracker;

    //取最后五次滑动的速度，提高精准度
    private int[] mVelocity = new int[5];

    private boolean isIntercept = false;

    private float mLastX, mLastY;

    private boolean isFirst = true;

    private CloseListener mListener;

    private int mCloseDistance = 0;

    //开关
    private boolean open = true;

    //滑动速度索引
    private int mIndex = 0;

    public ChatContentLayout(Context context) {
        super(context);
    }

    public ChatContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!open) return super.onInterceptTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = (int) ev.getX();
            mLastY = (int) ev.getY();
            isIntercept = false;
            isFirst  = true;
        }

        //当X滑动大于Y滑动时拦截
        if (mListener != null && (ev.getAction() == MotionEvent.ACTION_MOVE ||
                ev.getAction() == MotionEvent.ACTION_UP)) {
            if (isFirst) {
                float dx = Math.abs(ev.getX() - mLastX);
                float dy = Math.abs(ev.getY() - mLastY);
                if (dx - dy > 0) {
                    isFirst = false;
                    isIntercept = true;
                }
            }
            return isIntercept;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTracker = VelocityTracker.obtain();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mTracker.addMovement(event);
                mTracker.computeCurrentVelocity(500);
                mVelocity[mIndex] = (int) mTracker.getXVelocity();
                if (mIndex == 4) {
                    mIndex = 0;
                } else {
                    mIndex++;
                }
                break;
            case  MotionEvent.ACTION_UP:
                int avg = 0;
                for (int i : mVelocity) {
                    avg += i;
                }
                avg /= 5;
                if (avg >= mCloseDistance && mListener != null) {
                    mListener.onTrigger();
                }
                for (int i = 0; i < 5; i++) {
                    mVelocity[i] = 0;
                }
                break;
            default:
                break;
        }
        mTracker.clear();
        mTracker.recycle();
        return true;
    }

    public void setRefreshListener(CloseListener listener) {
        mListener = listener;
    }

    public void setCloseDistance(int distance) {
        mCloseDistance = distance;
    }

    public int getRefreshDistance() {
        return mCloseDistance;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public interface CloseListener {
        void onTrigger();
    }

}
