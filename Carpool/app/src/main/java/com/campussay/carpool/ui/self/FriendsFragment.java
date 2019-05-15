package com.campussay.carpool.ui.self;


import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.FriendsAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.ui.self.testbean.FriendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Zhangfan on 2019/4/8
 **/
public class FriendsFragment extends BaseFragment<FriendPresenter> implements FriendIView{

    private boolean isFirst = true;
    private ExpandableListView listView;
    private FriendsAdapter adapter;
    private TextView mNullTextView;
    private RelativeLayout mNullLayout;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_self_friends;
    }

    @Override
    protected FriendPresenter createPresenter() {
        return new FriendPresenter();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFirst){
            mPresenter.getFriendsList();
        }
    }

    @Override
    protected void initView(){
        refreshLayout = mView.findViewById(R.id.self_friend_fresh);
        listView = mView.findViewById(R.id.self_friend_elv);
        mNullTextView = mView.findViewById(R.id.self_friend_null_text);
        mNullLayout = mView.findViewById(R.id.self_friend_null);
        refreshLayout.setOnRefreshListener(mPresenter.getOnRefreshListener());
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);//设置刷新时的颜色
        adapter = new FriendsAdapter(this.getContext(),new ArrayList<>());
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(mPresenter.getOnRefreshListener());
        listView.setGroupIndicator(null);
        listView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (listView.isGroupExpanded(groupPosition)){
                listView.collapseGroup(groupPosition);
            }else{
                listView.expandGroup(groupPosition,true);
            }
            return true;
        });
        listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> false);
        mPresenter.getFriendsList();
    }

    @Override
    public void getFriendsList(List<FriendBean.Data> list) {
        if (list.size() == 0){
            if (mNullLayout.getVisibility() == View.GONE)
                mNullLayout.setVisibility(View.VISIBLE);
            mNullTextView.setOnClickListener(mPresenter.getOnClickListener());
        }else {
            if (mNullLayout.getVisibility() == View.VISIBLE)
                mNullLayout.setVisibility(View.GONE);
            adapter.setButtonClickListener(mPresenter.getItemButtonListener(getActivity()));
            //针对尾巴上的GroupItem和ChildItem无法展示的解决
            list.add(list.get(list.size() - 1));
            list.add(list.get(list.size() - 1));
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
        isFirst = false;
    }

    @Override
    public void isRefreshSuccess(String isSuccess) {
        //Toast.makeText(getContext(), isSuccess, Toast.LENGTH_SHORT).show();
        refreshLayout.setRefreshing(false);
    }

}
