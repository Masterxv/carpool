package com.campussay.carpool.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.campussay.carpool.ui.self.FriendsFragment;
import com.campussay.carpool.ui.self.MessageFragment;

/**
 * Create by Zhangfan on 2019/4/8
 **/
public class SelfViewPagerAdapter extends FragmentPagerAdapter {
    private static String[] tab = {"消息","好友"};

    public SelfViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new MessageFragment();
        }else{
            return new FriendsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab[position];
    }
}
