package com.campussay.carpool.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.campussay.carpool.R;
import com.campussay.carpool.ui.post.PostSearchRecyclerViewItem;

import java.util.List;

/**
 * create by WenJinG on 2019/4/13
 */
public class PostSearchRecyclerViewAdapter extends RecyclerView.Adapter<PostSearchRecyclerViewAdapter.ViewHolder> {

    private List<PostSearchRecyclerViewItem>list;
    private ItemClickListener itemClickListener;

    public PostSearchRecyclerViewAdapter(List<PostSearchRecyclerViewItem>list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_search_recyclerview_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position<=9){
            PostSearchRecyclerViewItem item = list.get(position);
            holder.searchResultTitle.setTag(position);
            holder.searchResultTitle.setText(item.getLocation());
            holder.searchResultDetail.setText(item.getDetailAddress());
        }
    }

    @Override
    public int getItemCount() {
        if (list.size()>11) {
            return 10;
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView searchResultTitle;
        private TextView searchResultDetail;
        public ViewHolder(View itemView) {
            super(itemView);
            searchResultTitle = itemView.findViewById(R.id.post_search_result_title);
            searchResultDetail = itemView.findViewById(R.id.post_search_result_detail);
            itemView.setOnClickListener(v -> {
                if(itemClickListener !=null){
                    itemClickListener.onItemClick((int) searchResultTitle.getTag());
                }
            });
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
