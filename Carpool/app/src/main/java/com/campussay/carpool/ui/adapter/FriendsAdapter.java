package com.campussay.carpool.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.self.OnItemButtonClickListener;
import com.campussay.carpool.ui.self.testbean.FriendBean;
import com.campussay.carpool.ui.self.testbean.SelfBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Create by Zhangfan on 2019/4/8
 **/
public class FriendsAdapter extends BaseExpandableListAdapter {

    private List<FriendBean.Data> list;
    private OnItemButtonClickListener listener;
    private LayoutInflater inflater;
    private Context mContext;


    public FriendsAdapter(Context context, List<FriendBean.Data> list){
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }
    public void setList(List<FriendBean.Data> list){
        this.list = list;
    }

    public void setButtonClickListener(OnItemButtonClickListener listener){this.listener = listener;}

    /*
     *
     * 时间戳转字符串
     * */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    @Override
    public int getGroupCount() {
        return list == null ? 0 :list.size() ;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getTeams() == null ? 0 : list.get(groupPosition).getTeams().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getTeams().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FriendsGroupViewHolder friendsGroupViewHolder = null;
        if (convertView == null){

            convertView = inflater.inflate(R.layout.item_self_friend_father,null,false);

            friendsGroupViewHolder = new FriendsGroupViewHolder();

            friendsGroupViewHolder.tv_name = convertView.findViewById(R.id.friend_father_tv_name);//展示好友id

            friendsGroupViewHolder.iv_friendHead = convertView.findViewById(R.id.friend_father_iv_head);//好友头像

            friendsGroupViewHolder.btn_chat = convertView.findViewById(R.id.friend_father_btn_chat);//私聊按钮

            friendsGroupViewHolder.first_item = convertView.findViewById(R.id.friend_father_first_item);

            friendsGroupViewHolder.tvDeparture = convertView.findViewById(R.id.item_child_departure);

            friendsGroupViewHolder.tvEnd = convertView.findViewById(R.id.item_child_end);

            friendsGroupViewHolder.tvTime = convertView.findViewById(R.id.item_child_time);

            friendsGroupViewHolder.iv_more = convertView.findViewById(R.id.item_father_more);

            convertView.setTag(friendsGroupViewHolder);

        }else{
            friendsGroupViewHolder = (FriendsGroupViewHolder) convertView.getTag();
        }
        if (groupPosition == getGroupCount() - 2 || groupPosition == getGroupCount() - 1)
        {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            if (convertView.getVisibility() == View.INVISIBLE) {
                convertView.setVisibility(View.VISIBLE);
            }
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.meizi7)//图片加载出来前，显示的图片
                .fallback( R.drawable.meizi7) //url为空的时候,显示的图片
                .error(R.drawable.meizi7);//图片加载失败后，显示的图片

        Glide.with(mContext)
                .load(list.get(groupPosition).getHeadUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .apply(options)
                .into(friendsGroupViewHolder.iv_friendHead);

        friendsGroupViewHolder.btn_chat.setFocusable(false);

        friendsGroupViewHolder.btn_chat.setClickable(true);

        if (list.get(groupPosition).getTeams().size() != 0){

            friendsGroupViewHolder.iv_more.setVisibility(View.VISIBLE);

            friendsGroupViewHolder.first_item.setVisibility(View.VISIBLE);

            friendsGroupViewHolder.tv_name.setText(list.get(groupPosition).getNickName());

            friendsGroupViewHolder.tvTime.setText(getDateToString
                    (Long.parseLong(list.get(groupPosition).getTeams()
                            .get(0).getDestinationTime()),
                            "yyyy-MM-dd HH:mm"));

            if (isExpanded){
                friendsGroupViewHolder.iv_more.setImageResource(R.drawable.item_up);
            }else{
                friendsGroupViewHolder.iv_more.setImageResource(R.drawable.item_down);
            }

            friendsGroupViewHolder.tvDeparture.setText(
                    list.get(groupPosition).getTeams().get(0).getStartName());

            friendsGroupViewHolder.tvEnd.setText(
                    list.get(groupPosition).getTeams().get(0).getEndName());
        }else{
            friendsGroupViewHolder.first_item.setVisibility(View.GONE);

            friendsGroupViewHolder.iv_more.setVisibility(View.GONE);
        }
        clickButton(friendsGroupViewHolder.btn_chat,groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FriendsChildViewHolder friendsChildViewHolder;
        if (convertView == null){

            convertView = inflater.inflate(R.layout.item_self_friends_child,null,false);

            friendsChildViewHolder = new FriendsChildViewHolder();

            convertView.setTag(friendsChildViewHolder);

            friendsChildViewHolder.tvDeparture = convertView.findViewById(R.id.item_child_departure);//出发点

            friendsChildViewHolder.tvTime = convertView.findViewById(R.id.item_child_time);//发布的当时时间

            friendsChildViewHolder.tvEnd = convertView.findViewById(R.id.item_child_end);//终点地

        }else{
            friendsChildViewHolder = (FriendsChildViewHolder) convertView.getTag();
        }
        //设置具体内容
       if (childPosition >= 1){

           friendsChildViewHolder.tvDeparture.setText(
                   list.get(groupPosition)
                           .getTeams()
                           .get(childPosition)
                           .getStartName());

           friendsChildViewHolder.tvEnd.setText(
                   list.get(groupPosition)
                           .getTeams()
                           .get(childPosition)
                           .getEndName());

           friendsChildViewHolder.tvTime.setText(
                   getDateToString(Long.parseLong(
                           list.get(groupPosition)
                                   .getTeams()
                                   .get(childPosition)
                                   .getDestinationTime()),"yyyy-MM-dd HH:mm:ss"));
       }
        return convertView;
    }

    private void clickButton(View v,int position){
        if (v != null) {
            v.setOnClickListener(v1 -> {
                if (listener != null){
                    listener.clickButton(position);
                }
            });
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //好友消息的外item布局
    static class FriendsGroupViewHolder{

        TextView tv_name;
        ImageView iv_friendHead;
        Button btn_chat;
        View first_item;
        TextView tvDeparture;
        TextView tvEnd;
        TextView tvTime;
        ImageView iv_more;
    }

    //好友消息的子item布局
    static class FriendsChildViewHolder {
        TextView tvDeparture;
        TextView tvEnd;
        TextView tvTime;

    }

}
