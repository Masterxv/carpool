package com.campussay.carpool.ui.recommend;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ShellActivity;
import com.campussay.carpool.ui.adapter.RecommendAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.ui.chat.ChatActivity;

import java.math.BigDecimal;

/**
 * create by WenJinG on 2019/4/6
 */
public class RecommendFragment extends BaseFragment<RecommendPresenter> implements RecommendViewList {
    RecommendAdapter recommendAdapter;

    ConstraintLayout noPeopleLinearLayout;

    ImageView noPeopleImageView;

    TextView noPeopleImageViewTv;

    TextView num, locationPlace;

    RecyclerView recyclerView;
    //进入群聊
    Button joinChat;

    //刷新:
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isFirst = true;

    private BigDecimal mLongitude;
    private BigDecimal mLatitude;
    private MutableLiveData<String> mTitle = new MutableLiveData<>();
    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {

//                if (mPresenter !=null&& mTitle!=null) {
//                    //获取推荐行程
//                    mPresenter.postLocation();
//                    if (!isFirst) mPresenter.recommendRoute();
//                    locationPlace.setText(mTitle.getValue() + "");
//                    if (num!=null) {
//                        num.setText("在线人数：" + RecommendPresenter.num);
//                    }
//                }

                if (recommendAdapter!=null) {
                    recommendAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
                if (num!=null) {
                    num.setText("在线人数：" + RecommendPresenter.num);
                }
                if (mTitle!=null) {
                    locationPlace.setText(mTitle.getValue() + "");
                }
            }
        }
    };


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected RecommendPresenter createPresenter() {
        return new RecommendPresenter();
    }

    @Override
    protected void initView() {
        noPeopleLinearLayout=mView.findViewById(R.id.no_recommend_xml);
        noPeopleImageView=mView.findViewById(R.id.no_recommend_view);
        locationPlace = mView.findViewById(R.id.recommend_location);
        num = mView.findViewById(R.id.recommend_master_name);
        joinChat = mView.findViewById(R.id.post_join);
        recyclerView = mView.findViewById(R.id.recommend_recyclerview);
        noPeopleImageViewTv = mView.findViewById(R.id.no_recommend_view_tv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        initLiveData();

        //获取推荐行程
        //mPresenter.postLocation();

        //刷新
        initSwipeRefresh();

        //跳转聊天
        joinChat.setOnClickListener(v -> {
            if (mTitle!=null) {
                ChatActivity.startPlaceChatting(mTitle.getValue(),
                        getActivity(),
                        CarpoolApplication.getAccount().getToken(),
                        CarpoolApplication.getAccount().getId(),
                        CarpoolApplication.getAccount().getHeadUrl());
            }else {
                Toast.makeText(getActivity(), "未获取到您的位置信息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initSwipeRefresh() {
        swipeRefreshLayout = mView.findViewById(R.id.recommend_swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);//设置刷新时的颜色

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mPresenter.isOnRequestRecommend){
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                swipeRefreshLayout.setRefreshing(true);
                mPresenter.recommendRoute();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = 1;
                        handle.sendMessage(message);
                    }
                }).start();
            }
        });

    }

    @Override
    public void getRecommendList(RecommendDataBean getRecommendData) {
        swipeRefreshLayout.setRefreshing(false);
        if (getRecommendData.getBriefTeamDTOS().size()!=0){
            if (noPeopleImageView.getVisibility() == View.VISIBLE) {
                noPeopleImageView.setVisibility(View.GONE);
                noPeopleImageViewTv.setVisibility(View.GONE);
            }
            noPeopleLinearLayout.setBackgroundColor(Color.parseColor("#f3f3f3"));
        }else {
            if (getRecommendData.getBriefTeamDTOS().size()==0) {
                if (noPeopleImageView.getVisibility() == View.GONE) {
                    noPeopleImageView.setVisibility(View.VISIBLE);
                    noPeopleImageViewTv.setVisibility(View.VISIBLE);
                }
                noPeopleLinearLayout.setBackgroundColor(Color.parseColor("#fbfbfb"));
            }
        }
        recommendAdapter = new RecommendAdapter(mView.getContext(),
                getRecommendData);
        recyclerView.setAdapter(recommendAdapter);
        recommendAdapter.notifyDataSetChanged();

        //加入推荐行程
        recommendAdapter.setOnClick(new RecommendOnClick() {
            @Override
            public void joinRoute(int position) {
                mPresenter.recommendApplyRoute(RecommendAdapter.teamId.get(position));
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            //fragment可见
            //得到位置信息
            if(!isFirst)
            if(locationPlace.getText().equals("null")){
                ((ShellActivity)getActivity()).initLocation();
            }
            if (mPresenter !=null&& mTitle!=null) {
                //获取推荐行程
                mPresenter.postLocation();
                if (!isFirst) mPresenter.recommendRoute();
                locationPlace.setText(mTitle.getValue() + "");
                if (num!=null) {
                    num.setText("在线人数：" + RecommendPresenter.num);
                }
            }
        }else {
            //fragment不可见

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public BigDecimal getLongitude() {
        return mLongitude;
    }

    public BigDecimal getLatitude() {
        return mLatitude;
    }

    public void showNum(String s){
        num.setText("在线人数："+s);
    }
    public void initLiveData() {
        mTitle.observe(this, s -> {
            locationPlace.setText(s);
            mPresenter.postLocation();
            if (isFirst) {
                mPresenter.recommendRoute();
                isFirst = false;
            }
        });
        ((ShellActivity) getActivity()).addLocationChangeListener(
                (latitude, longitude, title, city) -> {
            mLatitude = latitude;
            mLongitude = longitude;
            mTitle.setValue(title);
        });
    }
    public void showToast(String s){
        Toast.makeText(this.getContext(),s,Toast.LENGTH_SHORT).show();
    }
}
