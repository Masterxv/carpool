package com.campussay.carpool.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.campussay.carpool.ui.post.PostFragment;
import com.campussay.carpool.ui.recommend.RecommendFragment;
import com.campussay.carpool.ui.route.RouteFragment;
import com.campussay.carpool.ui.self.SelfFragment;

/**
 * create by WenJinG on 2019/4/6
 */
public class CarpoolViewPagerAdapter extends FragmentPagerAdapter {

    public static final String[]tab = new String[]{"推荐","发布","行程","我的"};

    public CarpoolViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tab.length;
    }

    @Override
    public Fragment getItem(int position) {
       if(position == 1){
           return new PostFragment();
       }else if(position == 2){
           return new RouteFragment();
       } else if ((position == 3)) {
           return new SelfFragment();
       }
       return new RecommendFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //不销毁Item
    }
}
