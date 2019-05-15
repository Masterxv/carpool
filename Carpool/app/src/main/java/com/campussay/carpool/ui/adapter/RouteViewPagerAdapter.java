package com.campussay.carpool.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campussay.carpool.ui.route.routeApply.RouteApplyFragment;
import com.campussay.carpool.ui.route.RouteLeader.RouteLeaderFragment;
import com.campussay.carpool.ui.route.routeMember.RouteMemberFargment;
import com.campussay.carpool.utils.LogUtils;

/**
 * creat by teng on 2019/4/11
 */
public class RouteViewPagerAdapter extends FragmentPagerAdapter {
    public static final String[]tab = new String[3];

    private RouteLeaderFragment mLeaderFragment = new RouteLeaderFragment();

    private RouteMemberFargment mMemberFragment = new RouteMemberFargment();

    private RouteApplyFragment mApplyFragment = new RouteApplyFragment();

    public RouteViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tab.length;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 1){
            LogUtils.d("第一个碎片加载");
            return mLeaderFragment;
        }else if(position == 2){
            LogUtils.d("第二个碎片加载");
            return mMemberFragment;
        }
        return mApplyFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab[position];
    }

    public void onVisiableToUser() {
        mLeaderFragment.onVisibleToUser();
        mMemberFragment.onVisibleToUser();
        mApplyFragment.onVisibleToUser();
    }

//    @Override
//    public float getPageWidth(int position) {
//        return (float)0.9;
//    }

}
