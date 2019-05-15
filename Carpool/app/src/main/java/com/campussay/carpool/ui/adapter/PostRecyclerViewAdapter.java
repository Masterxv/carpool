package com.campussay.carpool.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.post.PostRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * create by WenJinG on 2019/4/7
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {
    private List<PostRecyclerViewItem>itemList = new ArrayList<>();
    private JoinClickListener joinClickListener;

    public PostRecyclerViewAdapter(List<PostRecyclerViewItem>itemList){
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_recyclerview_user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostRecyclerViewItem item = itemList.get(position);
        holder.userName.setText(item.getUserName());
        holder.postTime.setText(item.getPostTime());
        holder.postRouteStart.setText(item.getPostRouteStart());
        holder.postRouteEnd.setText(item.getPostRouteEnd());
        holder.jion.setTag(position);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView postRouteStart;
        private TextView postRouteEnd;
        private TextView postTime;
        private Button jion;
        private TextView userName;
        public ViewHolder(View itemView) {
            super(itemView);
            postRouteStart = itemView.findViewById(R.id.post_route_start);
            postRouteEnd = itemView.findViewById(R.id.post_route_end);
            postTime = itemView.findViewById(R.id.post_time);
            jion = itemView.findViewById(R.id.post_join);
            userName = itemView.findViewById(R.id.post_username);
            jion.setOnClickListener(v -> {
                if(joinClickListener!= null){
                    joinClickListener.onJoinClick((int)jion.getTag());
                }
            });
        }
    }

    public void setJoinClickListener(JoinClickListener joinClickListener) {
        this.joinClickListener = joinClickListener;
    }

    public interface JoinClickListener{
        void onJoinClick(int position);
    }
}
