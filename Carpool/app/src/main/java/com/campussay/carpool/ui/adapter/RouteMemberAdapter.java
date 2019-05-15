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

import com.campussay.carpool.ui.route.ParentRecyclerView;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.ui.route.routeMember.RouteInformationItem;
import com.campussay.carpool.ui.route.routeMember.RouteMemeberOnClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/12
 */
public class RouteMemberAdapter extends ParentRecyclerView.Adapter<RecyclerView.ViewHolder> {
    //全部行程布局
    private List<RouteInformationBean.dataBean> routeInformationBeans;
    public static List<Integer> teamIdList;
    public static List<String> headerUrl;
    public static List<String> selfId;
    public static List<String> placeNmae;
    Context context;

    RouteMemeberOnClick routeMemeberOnClick;

    public RouteMemberAdapter(List<RouteInformationBean.dataBean> routeInformationBeans,Context context) {
        teamIdList = new ArrayList<>();
        this.context=context;
        headerUrl = new ArrayList<>();
        selfId = new ArrayList<>();
        placeNmae = new ArrayList<>();
        this.routeInformationBeans = routeInformationBeans;
    }

    public void setOnClick(RouteMemeberOnClick routeMemeberOnClick) {
        this.routeMemeberOnClick = routeMemeberOnClick;
    }

    class RouteMyViewHolder extends RecyclerView.ViewHolder {
        TextView time, firstPlace, endPlace, leader, member, over, com;
        ImageView delete;
        //imageView4队长小圆点，imageView5队友小圆点
        ImageView imageView4,imageView5;

        public RouteMyViewHolder(View itemView) {
            super(itemView);
            imageView4=itemView.findViewById(R.id.imageView4);
            imageView5=itemView.findViewById(R.id.imageView5);
            delete = itemView.findViewById(R.id.route_apply_my_delete);
            over = itemView.findViewById(R.id.route_apply_over);
            com = itemView.findViewById(R.id.route_apply_com);
            member = itemView.findViewById(R.id.route_apply_my_member);
            time = itemView.findViewById(R.id.route_apply_my_time);
            firstPlace = itemView.findViewById(R.id.route_apply_my_firstPlace);
            endPlace = itemView.findViewById(R.id.route_apply_my_endPlace);
            leader = itemView.findViewById(R.id.route_apply_my_leader);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_member, parent, false);
        RouteMyViewHolder viewHolder = new RouteMyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (routeInformationBeans.size() != 0) {
            if (!routeInformationBeans.get(position).isIsLeader()) {
                if (routeInformationBeans.get(position).getTeamId() != null) {

                    //性别 0 女 1 男 3未知
                    if (routeInformationBeans.get(position).getLeader().getSex()==0){
                        ((RouteMyViewHolder) holder).imageView4.setBackground(context.getResources().getDrawable(R.drawable.route_oval_purple));
                    }else if (routeInformationBeans.get(position).getLeader().getSex()==1){
                        ((RouteMyViewHolder) holder).imageView4.setBackground(context.getResources().getDrawable(R.drawable.route_oval_blue));
                    } else {
                        ((RouteMyViewHolder) holder).imageView4.setBackground(context.getResources().getDrawable(R.drawable.route_oval_gray));
                    }
                    if (routeInformationBeans.get(position).getMember().getSex()==0){
                        ((RouteMyViewHolder) holder).imageView5.setBackground(context.getResources().getDrawable(R.drawable.route_oval_purple));
                    }else if (routeInformationBeans.get(position).getMember().getSex()==1){
                        ((RouteMyViewHolder) holder).imageView5.setBackground(context.getResources().getDrawable(R.drawable.route_oval_blue));
                    } else {
                        ((RouteMyViewHolder) holder).imageView5.setBackground(context.getResources().getDrawable(R.drawable.route_oval_gray));
                    }


                    teamIdList.add(Integer.parseInt(routeInformationBeans.get(position).getTeamId()));
                    headerUrl.add(routeInformationBeans.get(position).getMember().getPhoto());
                    selfId.add(routeInformationBeans.get(position).getMember().getUserId());
                    placeNmae.add(deleteString(routeInformationBeans.get(position).getStartName())
                            + "->" + deleteString(routeInformationBeans.get(position).getEndName()));

                    ((RouteMyViewHolder) holder).member.setText(routeInformationBeans.get(position).getMember().getName());
                    ((RouteMyViewHolder) holder).time.setText(
                            new SimpleDateFormat("yyyy/MM/dd E HH:mm")
                                    .format(Double.parseDouble((routeInformationBeans.get(position).getDatetime()))));
                    ((RouteMyViewHolder) holder).firstPlace.setText(routeInformationBeans.get(position).getStartName());
                    ((RouteMyViewHolder) holder).endPlace.setText(routeInformationBeans.get(position).getEndName());
                    ((RouteMyViewHolder) holder).leader.setText(routeInformationBeans.get(position).getLeader().getName());

                    ((RouteMyViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            routeMemeberOnClick.delete(position);
                        }
                    });

                    //0代表未评价
                    if (routeInformationBeans.get(position).getEvaluate() == 0) {

                        ((RouteMyViewHolder) holder).over.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                routeMemeberOnClick.over(position);
                            }
                        });

                        ((RouteMyViewHolder) holder).over.setTextColor(
                                Color.parseColor("#444444"));
                    } else {
                        ((RouteMyViewHolder) holder).over.setTextColor(
                                Color.parseColor("#969696"));
                    }

                    ((RouteMyViewHolder) holder).com.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            routeMemeberOnClick.communication(position);
                        }
                    });


                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return routeInformationBeans.size();
    }


    //  删除数据
    public void removeData(int position) {
        routeInformationBeans.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public String deleteString(String n) {
        if (n.contains("(")) {
            String[] s1 = n.split("\\(");
            n=s1[0];
            return n;
        } else {
            return n;
        }
    }
}
