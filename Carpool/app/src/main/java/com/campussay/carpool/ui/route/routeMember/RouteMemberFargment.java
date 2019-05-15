package com.campussay.carpool.ui.route.routeMember;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.adapter.RouteLeaderAdapter;
import com.campussay.carpool.ui.adapter.RouteMemberAdapter;
import com.campussay.carpool.ui.base.BaseFragment;
import com.campussay.carpool.ui.chat.ChatActivity;
import com.campussay.carpool.ui.route.RouteInformationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/11
 */
public class RouteMemberFargment extends BaseFragment<RouteMemberPresenter> implements RouteMemberList{
    //行程评价
    // 1好评   2差评
    int evaluate;

    RecyclerView myRecyclerView;
    RouteMemberAdapter routeMemberAdapter;

    private boolean isFirst = true;

    //评价弹窗
    AlertDialog mEvaluateDialog;

    TextView noRouteMmeberTv;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_route_member;
    }

    @Override
    protected RouteMemberPresenter createPresenter() {
        return new RouteMemberPresenter();
    }

    @Override
    protected void initView() {

        noRouteMmeberTv=mView.findViewById(R.id.no_route_member_tv);
        myRecyclerView=mView.findViewById(R.id.route_recycler_apply_my);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mView.getContext());
        myRecyclerView.setLayoutManager(linearLayoutManager);
        mPresenter.getRouteMemberList();
    }

    @Override
    public void getRouteMemberList(List<RouteInformationBean.dataBean> routeInformationBeans) {
        if (isFirst) isFirst = false;

        List<RouteInformationBean.dataBean> list=new ArrayList<>();
        for (int i=0;i<routeInformationBeans.size();i++){
            if (!routeInformationBeans.get(i).isIsLeader()){
                list.add(routeInformationBeans.get(i));
            }
        }
        if (list.size()!=0){
            noRouteMmeberTv.setText("");
        }else {
            noRouteMmeberTv.setText("暂时还没有加入行程");
        }

        routeMemberAdapter = new RouteMemberAdapter(list,mView.getContext());
        routeMemberAdapter.setOnClick(new RouteMemeberOnClick() {
            @Override
            public void delete(int position) {
                //mPresenter.deleteRoute(position, RouteMemberAdapter.teamIdList.get(position))
                deleteDiaLogView(position);
            }

            @Override
            public void over(int position) {
                if (mEvaluateDialog != null && mEvaluateDialog.isShowing()) return;

                AlertDialog.Builder customDia = new AlertDialog.Builder(mView.getContext(),R.style.loadDialogTheme);
                final View viewDia= LayoutInflater.from(mView.getContext()).inflate(R.layout.dialog_evaluate, null);
                customDia.setView(viewDia);

                ImageView happy=viewDia.findViewById(R.id.iv_dialog_evaluate_my);
                happy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evaluate=1;
                        mPresenter.leaderRouteOver(RouteMemberAdapter.teamIdList.get(position),evaluate);
                    }
                });

                ImageView bad=viewDia.findViewById(R.id.iv_dialog_evaluate_sw);
                bad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evaluate=2;
                        mPresenter.leaderRouteOver(RouteMemberAdapter.teamIdList.get(position),evaluate);
                    }
                });
                mEvaluateDialog = customDia.create();
                mEvaluateDialog.show();
            }

            @Override
            public void communication(int position) {

                ChatActivity.startGroupChatting(RouteMemberAdapter.placeNmae.get(position) ,
                        getActivity(),
                        CarpoolApplication.getAccount().getToken(), Long.parseLong(RouteMemberAdapter.selfId.get(position))
                        , (RouteMemberAdapter.teamIdList.get(position)).longValue(), RouteMemberAdapter.headerUrl.get(position));

            }
        });
        myRecyclerView.setAdapter(routeMemberAdapter);
        routeMemberAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishEvaluate(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(CarpoolApplication.getApplication(),
                    "评价成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CarpoolApplication.getApplication(),
                    "网络繁忙", Toast.LENGTH_SHORT).show();
        }
        if (mEvaluateDialog != null && mEvaluateDialog.isShowing()) {
            mEvaluateDialog.dismiss();
        }
        mPresenter.getRouteMemberList();
    }

    @Override
    public void onFinishDelete(int position) {
        RouteMemberAdapter.teamIdList.remove(position);
        RouteMemberAdapter.placeNmae.remove(position);

        //Item里的删除
        //routeMemberAdapter.removeData(position);
        //外面的控件的删除
        routeMemberAdapter.removeData(position);
    }

    public void onVisibleToUser() {
        //fragment可见
        if (!isFirst) {
            mPresenter.getRouteMemberList();
        }
    }

    public void deleteDiaLogView(int position) {

//        AlertDialog.Builder customDia = new AlertDialog.Builder(mView.getContext(),R.style.loadDialogTheme);
//        final View viewDia = LayoutInflater.from(mView.getContext()).inflate(R.layout.dialog_delete,null);
//        customDia.setView(viewDia);
//
//        Button okBtn=viewDia.findViewById(R.id.delete_ok);
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.deleteRoute(position, RouteMemberAdapter.teamIdList.get(position));
//            }
//        });
//
//        Button cancelBtn=viewDia.findViewById(R.id.delete_cancel);
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        customDia.create().show();

        AlertDialog dialog = new AlertDialog.Builder(mView.getContext())
                .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                .setMessage("您确定要删除此行程吗？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteRoute(position, RouteMemberAdapter.teamIdList.get(position));
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }
}