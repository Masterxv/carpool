package com.campussay.carpool.ui.self;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.SelfViewPagerAdapter;
import com.campussay.carpool.ui.base.BaseFragment;

import java.lang.reflect.Field;

/**
 * create by WenJinG on 2019/4/6
 */
public class SelfFragment extends BaseFragment<SelfPresenter> {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView headImageView;
    private TextView name;
    private ImageView genderImageView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_self;
    }

    @Override
    protected SelfPresenter createPresenter() {
        return new SelfPresenter();
    }

    @Override
    protected void initView() {
        init();
        doSomething();
    }

    private void init(){
        mTabLayout = mView.findViewById(R.id.self_tab);
        mViewPager = mView.findViewById(R.id.self_view_pager);
        headImageView = mView.findViewById(R.id.self_iv_head);
        genderImageView = mView.findViewById(R.id.self_iv_gender);
        name = mView.findViewById(R.id.self_tv_id);
        mViewPager.setAdapter(new SelfViewPagerAdapter(getChildFragmentManager()));
        mTabLayout.setSelectedTabIndicatorHeight(10);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private void doSomething(){
        if (CarpoolApplication.getAccount() != null){
            Glide.with(getContext()).load(CarpoolApplication.getAccount().getHeadUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(headImageView);
           if (CarpoolApplication.getAccount().getGender() == 0){
               Glide.with(getContext()).load(R.mipmap.girl_32).into(genderImageView);
           }else{
               Glide.with(getContext()).load(R.mipmap.man_32).into(genderImageView);
           }
           name.setText(CarpoolApplication.getAccount().getName());
        }
    }

    @Override
    protected void getDataFromRoomAndShow() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
           setIndicator(mTabLayout,50,50);
            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

}
