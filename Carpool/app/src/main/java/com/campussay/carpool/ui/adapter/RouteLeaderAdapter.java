package com.campussay.carpool.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.route.ChildRecyclerView;
import com.campussay.carpool.ui.route.ParentRecyclerView;
import com.campussay.carpool.ui.route.RecyclerOnClick;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.ui.route.RouteLeader.GetParentPosition;
import com.campussay.carpool.ui.route.RouteLeader.RouteLeaderOnclick;
import com.campussay.carpool.ui.route.RouteLeader.ViewGone;
import com.campussay.carpool.ui.route.routeMember.RouteInformationItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/13
 */
public class RouteLeaderAdapter extends ParentRecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerOnClick recyclerOnClick;

    List<RouteInformationBean.dataBean> routeInformationBean;

    private ParentRecyclerView mParent;
    RouteLeaderOnclick routeLeaderOnclick;
    GetParentPosition getParentPosition;
    Context context;
    ViewGone viewGone;


    //自己的leader的id
    public static List<String> selfId;
    //队伍id
    public static List<Integer> teamId;
    //头像信息
    public static List<String> headerUrl;
    //起始位置信息
    public static List<String>placeList;

    public static List<List<Integer>>leaderListId;

    public void setOnClick(RouteLeaderOnclick routeLeaderOnclick) {
        this.routeLeaderOnclick = routeLeaderOnclick;
    }

    public RouteLeaderAdapter(List<RouteInformationBean.dataBean> routeInformationBean, ParentRecyclerView parent,Context context) {
        teamId = new ArrayList<>();
        this.context=context;
        selfId = new ArrayList<>();
        headerUrl = new ArrayList<>();
        placeList=new ArrayList<>();
        leaderListId=new ArrayList<>();
        this.routeInformationBean = routeInformationBean;
        mParent = parent;

        for (int i = 0; i < routeInformationBean.size(); i++) {
            selfId.add(routeInformationBean.get(i).getLeader().getUserId());
            teamId.add(Integer.parseInt(routeInformationBean.get(i).getTeamId()));
            headerUrl.add(routeInformationBean.get(i).getLeader().getPhoto());

            placeList.add(deleteString(routeInformationBean.get(i).getStartName())+"->"
                    +deleteString(routeInformationBean.get(i).getEndName()));

            if (routeInformationBean.get(i).getApplyList() != null) {
                List<Integer> l = new ArrayList<>();
                for (int j = 0; j < routeInformationBean.get(i).getApplyList().size(); j++) {
                    l.add(Integer.valueOf(routeInformationBean.get(i).getApplyList()
                            .get(j).getUserVO().getUserId()));
                }
                leaderListId.add(l);
            }
        }
    }

    public void setOnClick(RecyclerOnClick recyclerOnClick) {
        this.recyclerOnClick = recyclerOnClick;
    }

    public void getParentPosition(GetParentPosition getParentPosition) {
        this.getParentPosition = getParentPosition;
    }

    public void setViewGone(ViewGone viewGone) {
        this.viewGone = viewGone;
    }

    class RouteMyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView time, firstPlace, endPlace, leader, team, over, com, noPeopleApply;
        ChildRecyclerView applyRecycler;
        ImageView deeleteImageView;
        //view2:leader页的队长的小圆点
        //view7:leader页的队友的小圆点
        View view7,view2;

        public RouteMyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view2=itemView.findViewById(R.id.view2);
            view7=itemView.findViewById(R.id.view7);
            noPeopleApply = itemView.findViewById(R.id.no_apply_people_tv);
            deeleteImageView = itemView.findViewById(R.id.item_route_leader_delete);
            over = itemView.findViewById(R.id.route_leader_over);
            com = itemView.findViewById(R.id.route_leader_com);
            applyRecycler = itemView.findViewById(R.id.route_recycler_apply_now);
            applyRecycler.setOnDownListener(v -> mParent.setCurrentClickChild(v));

            team = itemView.findViewById(R.id.item_route_leader_team_name);
            time = itemView.findViewById(R.id.item_route_leader_time);
            firstPlace = itemView.findViewById(R.id.item_route_leader_firstPlace);
            endPlace = itemView.findViewById(R.id.item_route_leader_endPlace);
            leader = itemView.findViewById(R.id.item_route_leader_name);


            recyclerOnClick.Rcycler(applyRecycler);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_leader, parent, false);
        RouteMyViewHolder viewHolder = new RouteMyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (routeInformationBean.size() != 0) {

            if (routeInformationBean.get(position).isIsLeader()) {

                //性别 0 女 1 男 3未知
                if (routeInformationBean.get(position).getLeader().getSex()==0){
                    ((RouteMyViewHolder) holder).view2.setBackground(context.getResources().getDrawable(R.drawable.route_oval_purple));
                }else if (routeInformationBean.get(position).getLeader().getSex()==1){
                    ((RouteMyViewHolder) holder).view2.setBackground(context.getResources().getDrawable(R.drawable.route_oval_blue));
                } else {
                    ((RouteMyViewHolder) holder).view2.setBackground(context.getResources().getDrawable(R.drawable.route_oval_gray));
                }

                ((RouteMyViewHolder) holder).time.setText(
                        new SimpleDateFormat("yyyy/MM/dd E HH:mm").
                                format(Double.parseDouble((routeInformationBean.get(position).getDatetime()))));
                ((RouteMyViewHolder) holder).firstPlace.setText(routeInformationBean.get(position).getStartName());
                ((RouteMyViewHolder) holder).endPlace.setText(routeInformationBean.get(position).getEndName());
                ((RouteMyViewHolder) holder).leader.setText(routeInformationBean.get(position).getLeader().getName());
                if (routeInformationBean.get(position).getMember() == null) {

                } else if (routeInformationBean.get(position).getMember() != null) {

                    //性别 0 女 1 男 3未知
                    if (routeInformationBean.get(position).getMember().getSex()==0){
                        ((RouteMyViewHolder) holder).view7.setBackground(context.getResources().getDrawable(R.drawable.route_oval_purple));
                    }else if (routeInformationBean.get(position).getMember().getSex()==1){
                        ((RouteMyViewHolder) holder).view7.setBackground(context.getResources().getDrawable(R.drawable.route_oval_blue));
                    } else {
                        ((RouteMyViewHolder) holder).view7.setBackground(context.getResources().getDrawable(R.drawable.route_oval_gray));
                    }
                    ((RouteMyViewHolder) holder).team.setTextColor(Color.parseColor("#000000"));
                    ((RouteMyViewHolder) holder).team.setText(routeInformationBean.get(position).getMember().getName());
                }

                if (getParentPosition != null) {
                    getParentPosition.getPositionId(position);
                }

                if (viewGone != null) {
                    viewGone.MakeViewGone(((RouteMyViewHolder) holder).noPeopleApply, ((RouteMyViewHolder) holder).noPeopleApply);
                }

                if (routeInformationBean.get(position).getMember() != null) {
                    if ((((RouteMyViewHolder) holder).view7).getVisibility() == View.GONE) {
                        (((RouteMyViewHolder) holder).view7).setVisibility(View.VISIBLE);
                    }
                }

                if (routeInformationBean.get(position).getMember() != null) {
                    ((RouteMyViewHolder) holder).noPeopleApply.setText("你 已 经 有 队 友 啦 ~");
                }

                if (routeInformationBean.get(position).getMember() != null){

                    ((RouteMyViewHolder) holder).com.setTextColor(Color.parseColor("#444444"));
                    ((RouteMyViewHolder) holder).over.setTextColor(Color.parseColor("#444444"));

                    if (routeInformationBean.get(position).getEvaluate() == 0) {
                        ((RouteMyViewHolder) holder).over.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                routeLeaderOnclick.over(position);
                            }
                        });
                    } else {
                        ((RouteMyViewHolder) holder).over.setTextColor(Color.parseColor("#969696"));
                    }

                    if (routeInformationBean.get(position).getEvaluate() == 0) {

                        if (routeInformationBean.get(position).getMember()!=null) {
                            ((RouteMyViewHolder) holder).com.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    routeLeaderOnclick.over(position);
                                }
                            });
                        }

                    } else {

                    }
            }
                if (routeInformationBean.get(position).getMember()!=null) {
                    ((RouteMyViewHolder) holder).com.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            routeLeaderOnclick.communication(position);
                        }
                    });
                }

                ((RouteMyViewHolder) holder).deeleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        routeLeaderOnclick.delete(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int size=routeInformationBean.size();
        for (int i=0;i<routeInformationBean.size();i++){
            if (!routeInformationBean.get(i).isIsLeader()){
                size--;
            }
        }
        return size;
    }

    //  删除数据
    public void removeData(int position) {
        routeInformationBean.remove(position);
        //删除动画
        notifyItemRemoved(position);

        //刷新则会失去动画
        //notifyDataSetChanged();
    }
    public String deleteString(String n) {
        if (n.contains("(")) {
            String[] s1 = n.split("\\(");
            n = s1[0];
            return n;
        } else {
            return n;
        }
    }
}
