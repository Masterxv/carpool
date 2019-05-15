package com.campussay.carpool.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.route.RouteLeader.RouteTeamItem;

import java.util.List;

/**
 * creat by teng on 2019/4/11
 */
public class RouteTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<RouteTeamItem> routeTeamItems;

    public RouteTeamAdapter(List<RouteTeamItem> routeTeamItems) {
        this.routeTeamItems = routeTeamItems;
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tv_people;

        public RouteViewHolder(View itemView) {
            super(itemView);
            tv_people = itemView.findViewById(R.id.route_recycler_tv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_recycler_teammate, parent, false);
    RouteViewHolder routeViewHolder=new RouteViewHolder(view);
    return routeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RouteTeamItem routeTeamItem = routeTeamItems.get(position);
        ((RouteViewHolder) holder).tv_people.setText(routeTeamItem.getName());
    }

    @Override
    public int getItemCount() {
        return routeTeamItems.size();
    }
}
