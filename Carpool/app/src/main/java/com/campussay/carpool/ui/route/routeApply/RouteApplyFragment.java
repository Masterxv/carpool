package com.campussay.carpool.ui.route.routeApply;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.RouteApplyAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.ui.route.bean.ApplyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/11
 */
public class RouteApplyFragment extends BaseFragment<ApplyPresenter> implements ApplyIVew{
    private RecyclerView recyclerView;
    private RouteApplyAdapter adapter;
    private TextView mTextView;
    private ImageView ivNotApply;
    private boolean isFirst = true;

    private List<ApplyBean.Data> mMoreData = new ArrayList<>();

    private int mPage = 1;

    private boolean mHasMore = false;

    @Override
    protected int getContentViewId() {
        return R.layout.item_route_apply;
    }

    @Override
    protected ApplyPresenter createPresenter() {
        return new ApplyPresenter();
    }

    @Override
    protected void initView() {
        recyclerView = mView.findViewById(R.id.route_apply_rv);
        mTextView = mView.findViewById(R.id.route_apply_tv);
        ivNotApply = mView.findViewById(R.id.iv_not_apply);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RouteApplyAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);
        adapter.addLoadMoreClickListener(v -> {
            if (mMoreData.size() != 0) {
                adapter.addList(mMoreData);
                mMoreData.clear();
                mPresenter.getApplyList((++mPage + 1), true);
            }
        });

        mPresenter.getApplyList(mPage, false);
        mPresenter.getApplyList(mPage + 1, true);
    }

    @Override
    public void getApplyList(List<ApplyBean.Data> list, boolean isMorePage) {
        if (!isMorePage) {
            if (list.size() == 0) {
                if (recyclerView.getVisibility() == View.VISIBLE)
                    recyclerView.setVisibility(View.GONE);
                if (ivNotApply.getVisibility() == View.GONE)
                    ivNotApply.setVisibility(View.VISIBLE);
            } else {
                if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);
                if (ivNotApply.getVisibility() == View.VISIBLE)
                    ivNotApply.setVisibility(View.GONE);
                adapter.setList(list);
            }
        } else {
            if (list.size() == 0) adapter.setHasMore(false);
            else {
                adapter.setHasMore(true);
                mMoreData = list;
            }

        }
    }

    public void onVisibleToUser() {
        if (!isFirst){
            mPage = 1;
            mPresenter.getApplyList(mPage, false);
            mPresenter.getApplyList(mPage, true);
        } else isFirst = false;
    }
}
