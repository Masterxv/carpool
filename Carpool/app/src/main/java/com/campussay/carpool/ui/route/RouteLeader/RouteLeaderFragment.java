package com.campussay.carpool.ui.route.RouteLeader;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.RouteLeaderApplyAdapter;
import com.campussay.carpool.ui.adapter.RouteLeaderAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.ui.chat.ChatActivity;
import com.campussay.carpool.ui.route.ParentRecyclerView;
import com.campussay.carpool.ui.route.RecyclerOnClick;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/11
 */
public class RouteLeaderFragment extends BaseFragment<RouteLeaderPresenter> implements RouteLeaderBigList{
    //行程评价
    //   1好评   2差评
    int evaluate;

    public ParentRecyclerView myRecyclerView;
    //申请部分
    RouteLeaderApplyAdapter routeLeaderApplyAdapter;
    //出发行程信息
    RouteLeaderAdapter routeLeaderAdapter;

    //父linearLayoutManager
    LinearLayoutManager linearLayoutManager;

    //评价弹窗
    AlertDialog mEvaluateDialog;

    TextView no_people_show;

    private boolean isFirst = true;

    private int mCurrentItemIndex = 0;
    private boolean isCanMove = false;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_route_leader;
    }

    @Override
    protected RouteLeaderPresenter createPresenter() {
        return new RouteLeaderPresenter();
    }

    @Override
    protected void initView() {

        no_people_show=mView.findViewById(R.id.no_route_leader_tv);
        myRecyclerView = mView.findViewById(R.id.rv_route_leader);
        linearLayoutManager = new LinearLayoutManager(mView.getContext());
        myRecyclerView.setLayoutManager(linearLayoutManager);
        routeLeaderAdapter = new RouteLeaderAdapter(new ArrayList<>(), myRecyclerView,mView.getContext());
        mPresenter.getRouteLeaaderBigList();
    }

    @Override
    public void getRouteLeaderBigList(List<RouteInformationBean.dataBean> routeInformationBean) {
        if (isFirst) isFirst = false;

        List<RouteInformationBean.dataBean>list=new ArrayList<>();

        for (int i=0;i<routeInformationBean.size();i++){
            if (routeInformationBean.get(i).isIsLeader()){
                list.add(routeInformationBean.get(i));
            }
        }
        
        // 注意:
        // 一定是使用new Adapter的方式进行
        // 不然使用同一个 Adapter，不然是会内存泄漏的，“申请加入”的列表不会被清空！
        // 别问为什么，，，这里接口嵌套得太多了
        // 所以，接口回调要慎用，用了也要注意解绑，不然就只能使用整体替换了。
        routeLeaderAdapter = new RouteLeaderAdapter(list, myRecyclerView,mView.getContext());

        if (list.size()!=0){
            no_people_show.setText("");
        }else {
            no_people_show.setText("暂时还没有发布行程");
        }
        routeLeaderAdapter.setOnClick(new RouteLeaderOnclick() {
            @Override
            public void delete(int position) {
                //mPresenter.deleteRoute(RouteLeaderAdapter.teamId.get(position), position);
                deleteDiaLogView(position);
            }

            @Override
            public void over(int position) {
                if (mEvaluateDialog != null && mEvaluateDialog.isShowing()) return;

                AlertDialog.Builder customDia = new AlertDialog.Builder(mView.getContext(),R.style.loadDialogTheme);
                final View viewDia = LayoutInflater.from(mView.getContext()).inflate(R.layout.dialog_evaluate, null);
                customDia.setView(viewDia);

                ImageView happy = viewDia.findViewById(R.id.iv_dialog_evaluate_my);
                happy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evaluate = 1;
                        mPresenter.leaderRouteOver(position,
                                RouteLeaderAdapter.teamId.get(position),
                                evaluate);
                    }
                });

                ImageView bad = viewDia.findViewById(R.id.iv_dialog_evaluate_sw);
                bad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evaluate = 2;
                        mPresenter.leaderRouteOver(position,
                                RouteLeaderAdapter.teamId.get(position),
                                evaluate);
                    }
                });
                mEvaluateDialog = customDia.create();
                mEvaluateDialog.show();
            }

            @Override
            public void communication(int position) {
                if (RouteLeaderAdapter.placeList.size() != 0) {
                    ChatActivity.startGroupChatting(RouteLeaderAdapter.placeList.get(position),
                            getActivity(),
                            CarpoolApplication.getAccount().getToken(), Long.parseLong(RouteLeaderAdapter.selfId.get(position))
                            , (RouteLeaderAdapter.teamId.get(position)).longValue(), RouteLeaderAdapter.headerUrl.get(position));
                }else {
                    Toast.makeText(getActivity(), "未获取到队伍信息", Toast.LENGTH_SHORT).show();
                }
            }

        });

        routeLeaderAdapter.setOnClick(new RecyclerOnClick() {
            @Override
            public void Rcycler(RecyclerView recyclerView) {

                routeLeaderAdapter.getParentPosition(new GetParentPosition() {

                    // position1：父view的position
                    @Override
                    public void getPositionId(int position1) {

                        //如果申请数据不为空
                        if (list.get(position1).getApplyList().size() != 0) {
                            showView(false);
                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mView.getContext());
                            recyclerView.setLayoutManager(linearLayoutManager1);
                            routeLeaderApplyAdapter = new RouteLeaderApplyAdapter(list.get(position1).getApplyList());
                            //是否选择
                            routeLeaderApplyAdapter.setOnClick(new RouteLeaderApplyChoose() {
                                @Override
                                //position 子view的position
                                public void isChoose(int position) {
                                    int itemPosition=position1;
                                    mPresenter.applyRoute(
                                            RouteLeaderAdapter.teamId.get(position1),
                                            RouteLeaderAdapter.leaderListId.get(position1).get(position),
                                            itemPosition);
                                }
                            });
                            recyclerView.setAdapter(routeLeaderApplyAdapter);
                            routeLeaderApplyAdapter.notifyDataSetChanged();
                        } else {
                            RouteLeaderAdapter.leaderListId.add(new ArrayList<Integer>());

                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mView.getContext());
                            recyclerView.setLayoutManager(linearLayoutManager1);
                            routeLeaderApplyAdapter = new RouteLeaderApplyAdapter(new ArrayList<>());
                            recyclerView.setAdapter(routeLeaderApplyAdapter);
                            routeLeaderApplyAdapter.notifyDataSetChanged();
                            showView(true);
                        }
                    }
                });
            }
        });

        myRecyclerView.setAdapter(routeLeaderAdapter);
        routeLeaderAdapter.notifyDataSetChanged();
        if (isCanMove) moveToPosition(linearLayoutManager, myRecyclerView, mCurrentItemIndex);
        isCanMove = false;
    }


    public void onVisibleToUser() {
        //fragment可见
        if (!isFirst) {
            mPresenter.getRouteLeaaderBigList();
        }
    }

    @Override
    public void initDelete(int position){
        RouteLeaderAdapter.teamId.remove(position);
        RouteLeaderAdapter.placeList.remove(position);
        RouteLeaderAdapter.headerUrl.remove(position);
        RouteLeaderAdapter.selfId.remove(position);
        RouteLeaderAdapter.leaderListId.remove(position);

        //外面的控件的删除
        routeLeaderAdapter.removeData(position);
        if (mPresenter!=null) {
            mPresenter.getRouteLeaaderBigList();
        }
        if (routeLeaderAdapter != null) {
            routeLeaderAdapter.notifyDataSetChanged();
        }

        //记录位置
        mCurrentItemIndex = position;
        isCanMove = true;
    }

    @Override
    public void finishEvaluate(int position, boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(CarpoolApplication.getApplication(),
                    "评价成功", Toast.LENGTH_SHORT).show();
            mCurrentItemIndex = position;
            isCanMove = true;
            mPresenter.getRouteLeaaderBigList();
        } else {
            Toast.makeText(CarpoolApplication.getApplication(),
                    "网络繁忙", Toast.LENGTH_SHORT).show();
        }
        if (mEvaluateDialog != null && mEvaluateDialog.isShowing()) {
            mEvaluateDialog.dismiss();
        }
    }

    @Override
    public void finishAgreeJoin(boolean isSuccess, int itemPosition) {
        if (isSuccess) {
            mPresenter.getRouteLeaaderBigList();
            mCurrentItemIndex = itemPosition;
            isCanMove = true;
        } else {
            Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * true:显示view
     * false:隐藏view
     * @param isView
     */
    public void showView(boolean isView){
        if (isView) {
            if (routeLeaderAdapter!=null){
            routeLeaderAdapter.setViewGone(new ViewGone() {
                @Override
                public void MakeViewGone(View view, TextView textView) {
                    if (view.getVisibility() == View.GONE) {
                        view.setVisibility(View.VISIBLE);
                    }
                    }
                });
            }
        } else {
            if (routeLeaderAdapter!=null) {
                routeLeaderAdapter.setViewGone(new ViewGone() {
                    @Override
                    public void MakeViewGone(View view, TextView textView) {
                        if (view.getVisibility() == View.VISIBLE) {
                            view.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }

    /**
     * RecyclerView 移动到当前位置，
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {

        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }
    public void deleteDiaLogView(int position) {

//        AlertDialog.Builder customDia = new AlertDialog.Builder(getActivity());
//        final View viewDia = LayoutInflater.from(mView.getContext()).inflate(R.layout.dialog_delete,null);
//        customDia.setView(viewDia);
//
//        Button okBtn=viewDia.findViewById(R.id.delete_ok);
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.deleteRoute(RouteLeaderAdapter.teamId.get(position), position);
//                //initDelete(position);
//                AlertDialog.Builder customDia = new AlertDialog.Builder(v.getContext());
//                AlertDialog dialog=customDia.show();
//                dialog.dismiss();
//            }
//        });
//
//        Button cancelBtn=viewDia.findViewById(R.id.delete_cancel);
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder customDia = new AlertDialog.Builder(getActivity());
//                AlertDialog dialog=customDia.show();
//                dialog.dismiss();
//
//            }
//        });
//        customDia.create().show();


        AlertDialog dialog = new AlertDialog.Builder(mView.getContext())
                .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                .setMessage("您确定要删除此行程吗？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteRoute(RouteLeaderAdapter.teamId.get(position), position);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
