package com.campussay.carpool.ui.route;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * creat by teng on 2019/4/13
 */
public class ParentRecyclerView extends RecyclerView {

    private RecyclerView mCurrentClickChild;

    private float mRecordY = -1;

    private boolean onChildScroll = false;

    public ParentRecyclerView(Context context) {
        super(context);
        init();
    }

    public ParentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private int position=0;

    public ParentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private void init() {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.setLayoutManager(llm);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);//居中显示RecyclerView
        //this.addItemDecoration(new DividerItemDecoration(this.getContext(),
          //      DividerItemDecoration.HORIZONTAL));
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager){
                    int firs = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    if (position != firs){
                        position = firs;
                        if (onpagerChageListener != null)
                            onpagerChageListener.onPagerChage(position);
                    }
                }
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean a = super.onTouchEvent(e);
        return a;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            onChildScroll = false;
            mRecordY = e.getY();
            //之所以调用自己的拦截方法，因为
            return super.onInterceptTouchEvent(e);
        }

        if (e.getAction() != MotionEvent.ACTION_DOWN) {
            // 当前点击的孩子view
            // view正在滑动或者view到底部并且是手指向上滑动时 自己处理
            if (mCurrentClickChild != null) {
                if (onChildScroll
                        || (!mCurrentClickChild.canScrollVertically(1)
                        && e.getY() - mRecordY < 0)) {
                    onChildScroll = true;
                    return super.onInterceptTouchEvent(e);
                }

                if (onChildScroll
                        || (!mCurrentClickChild.canScrollVertically(-1)
                        && e.getY() - mRecordY > 0)) {
                    onChildScroll = true;
                    return super.onInterceptTouchEvent(e);
                }
            }
        }
        return false;
    }

    public void setOnPagerPosition(int position){
//        this.position = position;
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        layoutManager.scrollToPosition(position);
    }

    public int getOnPagerPosition(){
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        return ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
    }


    interface onPagerChageListener{
        void onPagerChage(int position);
    }

    private onPagerChageListener onpagerChageListener;
    public void setOnPagerChageListener(onPagerChageListener onpagerChageListener){
        this.onpagerChageListener = onpagerChageListener;
    }
    public void setCurrentClickChild(RecyclerView rv) {
        mCurrentClickChild = rv;
    }
}
