package com.campussay.carpool.ui.route;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.RouteViewPagerAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * create by WenJinG on 2019/4/6
 */
public class RouteFragment extends BaseFragment<RoutePresenter> {

    private ViewPager routeViewpager;
    private RouteViewPagerAdapter routeViewPagerAdapter;
    //定义一个点集合
    private List<View> dots;
    private int oldPosition = 0;// 记录上一次点的位置
    private int currentItem; // 当前页面位置

    private View  oval1,oval2,oval3;
    //创建一个list集合 参数为view
    private List<View> Mview = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_route;
    }

    @Override
    protected RoutePresenter createPresenter() {
        return new RoutePresenter();
    }

    @Override
    protected void initView() {

        dots = new ArrayList<View>();
        dots.add(mView.findViewById(R.id.oval1));
        dots.add(mView.findViewById(R.id.oval2));
        dots.add(mView.findViewById(R.id.oval3));

        routeViewpager= mView.findViewById(R.id.route_viewPager);

        //并且，默认第一个是选中状态
        dots.get(1).setBackgroundResource(R.drawable.route_oval);

        //把三个View布局对象加载到list中，这些就是item的数据
        Mview.add(oval1);
        Mview.add(oval2);
        Mview.add(oval3);

        routeViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {

                //下面就是获取上一个位置，并且把点的状体设置成默认状体
                dots.get(oldPosition).setBackgroundResource(R.drawable.route_oval_white);
                //获取到选中页面对应的点，设置为选中状态
                dots.get(position).setBackgroundResource(R.drawable.route_oval);
                //下面是记录本次的位置，因为在滑动，他就会变成过时的点了
                oldPosition = position;
                //关联页卡
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        routeViewPagerAdapter=new RouteViewPagerAdapter(getChildFragmentManager());
        //设置缓存页数
        routeViewpager.setOffscreenPageLimit(2);


        //设置view pager间距

        routeViewpager.setPageMargin(40);
        LogUtils.d("第碎片设置间距加载");

//        ViewGroup.LayoutParams params4 = routeViewpager.getLayoutParams();
//        params4.height=544;
//        routeViewpager.setLayoutParams(params4);

        routeViewpager.setAdapter(routeViewPagerAdapter);
        routeViewpager.setCurrentItem(1);

    }

    @Override
    public void onResume() {
        super.onResume();
        routeViewPagerAdapter.onVisiableToUser();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && routeViewPagerAdapter != null) {
            //fragment可见
            routeViewPagerAdapter.onVisiableToUser();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
