package com.campussay.carpool.ui.adapter;


/**
 * Create by Zhangfan on 2019/4/15
 **/
import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.self.OnItemButtonClickListener;
import com.campussay.carpool.ui.self.testbean.FriendBean;
import com.campussay.carpool.ui.self.view.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;
import static com.campussay.carpool.utils.DateUtil.getDateToString;


public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {

    private List<FriendBean.Data> list = new ArrayList<>();
    private OnItemButtonClickListener listener;
    private Context context;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;

    public PinnedHeaderExpandableAdapter(List<FriendBean.Data> list,Context context,PinnedHeaderExpandableListView listView){
        this.list = list;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.context);
    }

    public void setButtonClickListener(OnItemButtonClickListener listener){this.listener = listener;}

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getTeams().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createChildrenView();
        }
        //findbyid....
        TextView tvDeparture = view.findViewById(R.id.item_child_departure);//出发点
        TextView tvTime = view.findViewById(R.id.item_child_time);//发布的当时时间
        TextView tvEnd = view.findViewById(R.id.item_child_end);//终点地

        //设置具体内容
        if (childPosition >= 1){
            tvDeparture.setText(list.get(groupPosition).getTeams().get(childPosition).getStartName());
            tvEnd.setText(list.get(groupPosition).getTeams().get(childPosition).getEndName());
            tvTime.setText(list.get(groupPosition).getTeams().get(childPosition).getDestinationTime());
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getTeams().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }
        TextView tv_name = view.findViewById(R.id.friend_father_tv_name);//展示好友id
        ImageView iv_friendHead = view.findViewById(R.id.friend_father_iv_head);//好友头像
        Button btn_chat = view.findViewById(R.id.friend_father_btn_chat);//私聊按钮
        View first_item = view.findViewById(R.id.friend_father_first_item);
        TextView tvDeparture = view.findViewById(R.id.item_child_departure);
        TextView tvEnd = view.findViewById(R.id.item_child_end);
        TextView tvTime = view.findViewById(R.id.item_child_time);

        if (list.get(groupPosition).getHeadUrl() != null) {
            Glide.with(context)
                    .load(list.get(groupPosition).getHeadUrl())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_friendHead);
        }
        btn_chat.setFocusable(false);
        btn_chat.setClickable(true);

        if (list.get(groupPosition).getTeams().size() != 0){
            first_item.setVisibility(View.VISIBLE);
            tv_name.setText(list.get(groupPosition).getNickName());
            tvTime.setText(getDateToString
                    (Long.parseLong(list.get(groupPosition).getTeams()
                                    .get(0).getDestinationTime()),
                            "yyyy-MM-dd HH:mm:ss"));
            tvDeparture.setText(
                    list.get(groupPosition).getTeams().get(0).getStartName());
            tvEnd.setText(
                    list.get(groupPosition).getTeams().get(0).getEndName());
        }else{
            first_item.setVisibility(View.GONE);
        }
        clickButton(btn_chat,groupPosition);

        return view;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.item_self_friends_child, null);
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.item_self_friend_father, null);
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {
    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition)>=0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }
}
