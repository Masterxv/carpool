package com.campussay.carpool.ui.chat.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.campussay.carpool.R;
import com.campussay.carpool.utils.ScreenAttrUtil;

/**
 * create by zuyuan on 2019/4/21
 * 带刷新监听的ViewGroup
 * 注意: 第一个View是ImageView
 *      第二个View是RecyclerView
 */
public class ChatFrameLayout extends FrameLayout {

    private RecyclerView mHeadView;

    private ImageView ivRefresh;

    private float mStartY;

    //打开刷新
    private boolean mOpenRefresh ;

    //正在刷新
    private boolean mRefreshing = false;

    private OnRefreshListener mListener;

    private boolean mIntercept;

    //当前滑动记录的Y
    private int mRecordY ;

    public float MAX_OFFSET = 10 * ScreenAttrUtil.getDensity();

    public ChatFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ChatFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivRefresh = (ImageView) getChildAt(1);
        mHeadView = (RecyclerView) getChildAt(0);
        Glide.with(getContext()).asGif().load(R.drawable.gif_chat_load).into(ivRefresh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mHeadView == null) return super.onInterceptTouchEvent(ev);

        //不跟RecyclerView的滑动冲突
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mStartY = ev.getY();
            mIntercept = false;
        }
        if (mIntercept  //到底部的时候
                || (!mHeadView.canScrollVertically(-1)
                && ev.getAction() == MotionEvent.ACTION_MOVE
                && ev.getY() - mStartY > 0)) {
            mIntercept = true;
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //正在刷新的话则不能够进行操作
        if (mRefreshing) return super.onTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float oy = ev.getY() - mStartY;
                int oyi = (int) (oy * 2f / 3f);
                smoothScrollRefreshView(oyi);
                break;
            case MotionEvent.ACTION_UP:
                onKeyUp(mRecordY);
                mIntercept = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void smoothScrollRefreshView(int y) {
        final int height = ivRefresh.getMeasuredHeight();
        int oy = -height + y;
        if (oy <= -height) {
            return;
        } else if (oy >= 0) {
            mOpenRefresh = true;
            return;
        }

        //只有被滑动了的才会被记录
        mRecordY = y;
        ivRefresh.scrollTo(0, -oy);
    }

    private void onKeyUp(int y) {
        final int height = ivRefresh.getMeasuredHeight();
        int oy = -height + y;

        if (mOpenRefresh) {
            //刷新
            scrollIV(-oy, (int) (25 * ScreenAttrUtil.getDensity()));
        } else {
            //收回
            scrollIV(-oy, height);
        }
    }

    public void hide() {
        mRefreshing = false;
        mOpenRefresh = false;
        scrollIV((int) (25 * ScreenAttrUtil.getDensity()), ivRefresh.getMeasuredHeight());
    }

    private void scrollIV(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(400);
        animator.addUpdateListener(animation -> {
            int v = (int) animation.getAnimatedValue();
            ivRefresh.scrollTo(0,  v);
            //动画执行结束再刷新
            if (v == end) {
                if (mOpenRefresh) {
                    if (mListener != null) mListener.onRefresh();
                    mRefreshing = true;
                }
            }
        });
        animator.start();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
