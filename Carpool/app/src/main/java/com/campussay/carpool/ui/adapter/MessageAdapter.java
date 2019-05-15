package com.campussay.carpool.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.self.ItemLongClickListener;
import com.campussay.carpool.ui.self.MyItemClickListener;
import com.campussay.carpool.ui.self.testbean.MessagesBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Zhangfan on 2019/4/10
 **/
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_FRIEND = 0;//好友私信
    public static final int TYPE_STROKE = 1;
    private Context mContext;
    private LayoutInflater inflater;
    private MyItemClickListener listener;
    private List<MessagesBean.Data> data = new ArrayList<>();
    private List<MessagesBean.Data> cacheList = new ArrayList<>();
    private ItemLongClickListener longClickListener;

    @Override
    public int getItemViewType(int position) {
        if (data.size() != 0){
            return data.get(position).getType();
        }else {
            return -1;
        }
    }

    public MessageAdapter(Context context, List<MessagesBean.Data> data){
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    public void setList(List<MessagesBean.Data> list){
        this.data = list;
    }

    public void removeItem(int position){data.remove(position);notifyItemRemoved(position);notifyDataSetChanged();}

    public void setClickListener(MyItemClickListener listener){
        this.listener = listener;
    }

    public void setLongClickListener(ItemLongClickListener listener){this.longClickListener = listener;}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FRIEND){
            View view = inflater.inflate(R.layout.item_self_message_friend,parent,false);
            FriendViewHolder holder = new FriendViewHolder(view);
            onClick(holder.itemView);
            onLongClick(holder.itemView);
            return holder;
        }else if (viewType == TYPE_STROKE){
            View view = inflater.inflate(R.layout.item_self_message_stroke,parent,false);
            StrokeViewHolder holder = new StrokeViewHolder(view);
            onClick(holder.itemView);
            onLongClick(holder.itemView);
            return holder;
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.itemView.getVisibility() == View.GONE)
            holder.itemView.setVisibility(View.VISIBLE);
        if (holder instanceof StrokeViewHolder){
            StrokeViewHolder strokeViewHolder = (StrokeViewHolder) holder;
            strokeViewHolder.itemView.setTag(position);
            initStroke(strokeViewHolder,position);
        }else if (holder instanceof FriendViewHolder){
            FriendViewHolder friendViewHolder = (FriendViewHolder) holder;
            friendViewHolder.itemView.setTag(position);
            initFriend(friendViewHolder,position);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private void initStroke(StrokeViewHolder holder,int position){
        if (data.size() != 0){
            holder.tv_start.setText(data.get(position).getTheme());//起点
            holder.tv_end.setText(data.get(position).getSencondTheam());//终点
            if (data.get(position).getUnReadNum() > 0){
                holder.tv_numb.setVisibility(View.VISIBLE);
                holder.tv_numb.setText("" + data.get(position).getUnReadNum());
                holder.tv_numb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.tv_numb.setVisibility(View.GONE);
                    }
                });
            }else{
                holder.tv_numb.setVisibility(View.GONE);
            }

        }
    }

    private void initFriend(FriendViewHolder holder,int position){
        if (data.size() != 0){
            holder.tv_name.setText(data.get(position).getTheme());//好友名
            holder.tv_message.setText(data.get(position).getLastContent());//获取一条最新发送的消息
            if (data.get(position).getUnReadNum() > 0){
                holder.tv_numb.setVisibility(View.VISIBLE);
                holder.tv_numb.setText(""+ data.get(position).getUnReadNum());//获取还未读的消息数量
                holder.tv_numb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.tv_numb.setVisibility(View.GONE);
                    }
                });
            }else{
                holder.tv_numb.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(data.get(position).getHeadUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(holder.iv_head);

        }else{
            Glide.with(mContext)
                    .load(R.drawable.meizi7)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(holder.iv_head);
        }

    }

    private void onClick(View view){
        if (view != null){
            view.setOnClickListener(v -> {
                if (listener != null){
                    listener.click(v,(int) v.getTag());
                }

            });
        }
    }

    private void onLongClick(View view){
        view.setOnLongClickListener(v -> {
            longClickListener.delete(v,(int)v.getTag());
            return true;
        });
    }

    class StrokeViewHolder extends RecyclerView.ViewHolder{
        TextView tv_start;
        TextView tv_numb;
        TextView tv_end;

        public StrokeViewHolder(View itemView) {
            super(itemView);
            tv_numb = itemView.findViewById(R.id.item_messages_numb);
            tv_start = itemView.findViewById(R.id.item_messages_departure);
            tv_end = itemView.findViewById(R.id.item_messages_end);
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_head;
        TextView tv_name;
        TextView tv_numb;
        TextView tv_message;
        public FriendViewHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.item_friend_head);
            tv_numb = itemView.findViewById(R.id.item_friend_numb);
            tv_name = itemView.findViewById(R.id.item_friend_name);
            tv_message = itemView.findViewById(R.id.item_friend_message);
        }
    }
}
