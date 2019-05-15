package com.campussay.carpool.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.ui.route.RouteLeader.RouteLeaderApplyItem;
import com.campussay.carpool.ui.route.ChildRecyclerView;
import com.campussay.carpool.ui.route.RouteLeader.RouteLeaderApplyChoose;
import com.campussay.carpool.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/11
 */
public class RouteLeaderApplyAdapter extends ChildRecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RouteInformationBean.dataBean.ApplyListBean> routeInformationBeans;

    RouteLeaderApplyChoose routeLeaderApplyChoose;

    public static List<Integer>memberListId;

    public void setOnClick(RouteLeaderApplyChoose routeLeaderApplyChoose) {
        this.routeLeaderApplyChoose = routeLeaderApplyChoose;
    }

    public RouteLeaderApplyAdapter(List<RouteInformationBean.dataBean.ApplyListBean> routeInformationBeans) {
        memberListId=new ArrayList<>();
        for (RouteInformationBean.dataBean.ApplyListBean b : routeInformationBeans) {
            memberListId.add(Integer.parseInt(b.getUserVO().getUserId()));
        }
        this.routeInformationBeans = routeInformationBeans;
    }

    class OtherApplyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, ischoose;
        View vSex;

        public OtherApplyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.route_recycler_apply_other_image);
            name = itemView.findViewById(R.id.route_recycler_apply_other_tv);
            ischoose = itemView.findViewById(R.id.route_recycler_apply_other_tv_cho);
            vSex = itemView.findViewById(R.id.route_recycler_apply_other_sex_v);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_recycler_apply_other, parent, false);
        OtherApplyViewHolder viewHolder = new OtherApplyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (routeInformationBeans.size()!=0) {
            ImageView iv=
                    ((OtherApplyViewHolder) holder).imageView;
            Glide.with(CarpoolApplication.getApplication())
                    .load(routeInformationBeans.get(position).getUserVO().getPhoto())
                    .error(Glide.with(CarpoolApplication.getApplication()).load(R.drawable.ic_fail2))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv);

            ((OtherApplyViewHolder) holder).name.setText(routeInformationBeans.get(position).getUserVO().getName());
            ((OtherApplyViewHolder) holder).ischoose.setText("选我");

            ((OtherApplyViewHolder) holder).ischoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    routeLeaderApplyChoose.isChoose(position);
                }
            });

            //0 女 1男 3未知
            if (routeInformationBeans.get(position).getUserVO().getSex() == 0) {
                ((OtherApplyViewHolder) holder).vSex.setBackground(
                        CarpoolApplication.getApplication().getDrawable(R.drawable.route_oval_purple));
            } else if (routeInformationBeans.get(position).getUserVO().getSex() == 1){
                ((OtherApplyViewHolder) holder).vSex.setBackground(
                        CarpoolApplication.getApplication().getDrawable(R.drawable.route_oval_blue));
            } else {
                ((OtherApplyViewHolder) holder).vSex.setBackground(
                        CarpoolApplication.getApplication().getDrawable(R.drawable.route_oval_gray));
            }
        }
    }

    @Override
    public int getItemCount() {
        return routeInformationBeans.size();
    }
}
