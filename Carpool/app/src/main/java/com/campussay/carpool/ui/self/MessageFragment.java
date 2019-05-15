package com.campussay.carpool.ui.self;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.MessageAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.ui.chat.ChatActivity;
import com.campussay.carpool.ui.self.testbean.MessagesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Zhangfan on 2019/4/8
 **/
public class MessageFragment extends BaseFragment<MessagePresenter> implements MessageIView{
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout messageNullLayout;
    private TextView mNullTextView;
    private boolean isFirst = true;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_self_message;
    }

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFirst) {
            mPresenter.getMessageList();
        }
    }

    @Override
    protected void initView(){
        recyclerView = mView.findViewById(R.id.self_messages_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = mView.findViewById(R.id.self_messages_fresh);
        messageNullLayout = mView.findViewById(R.id.self_message_null);
        mNullTextView = mView.findViewById(R.id.self_message_null_text);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
         //       DividerItemDecoration.VERTICAL));
        refreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);//设置刷新时的颜色

        refreshLayout.setOnRefreshListener(mPresenter.getRefreshMessageList());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) mPresenter.getMessageList();
    }

    @Override
    public void getMessageList(List<MessagesBean.Data> list) {
        if (isFirst){
            adapter = new MessageAdapter(getContext(),list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
        if (list.size() == 0){
            messageNullLayout.setVisibility(View.VISIBLE);
            mNullTextView.setOnClickListener(mPresenter.getOnClickListener());
        }else{
            messageNullLayout.setVisibility(View.GONE);
            adapter.setClickListener(mPresenter.getMessageItemListener(getActivity(),adapter));
            adapter.setLongClickListener(mPresenter.getLongListener(getContext(),adapter));
        }
        isFirst = false;
    }

    @Override
    public void isRefreshSuccess(String isSuccess) {
        //Toast.makeText(getContext(),isSuccess,Toast.LENGTH_SHORT).show();
        if (refreshLayout.isRefreshing())
        refreshLayout.setRefreshing(false);
    }
}
