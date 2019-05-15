package com.campussay.carpool.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campussay.carpool.R;
import com.campussay.carpool.ui.recommend.RecommendDataBean;
import com.campussay.carpool.ui.recommend.RecommendOnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * creat by teng on 2019/4/6
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater mInflater;

    //行程列表
    private RecommendDataBean recommendDataBeans;

    public static List<Integer>teamId;

    RecommendOnClick recommendOnClick;
    public void setOnClick(RecommendOnClick recommendOnClick){
        this.recommendOnClick=recommendOnClick;
    }

    public RecommendAdapter
            (Context context,RecommendDataBean recommendDataBeans){
        teamId=new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.recommendDataBeans= recommendDataBeans;
    }

    class RoadViewHolder extends RecyclerView.ViewHolder{
        TextView tv_firstPlace,tv_endPlace,tv_go_time,tv_master,join;
        public RoadViewHolder(View itemView) {
            super(itemView);
            join=itemView.findViewById(R.id.post_join);
            tv_firstPlace=itemView.findViewById(R.id.recommend_firstPlace_tv);
            tv_endPlace=itemView.findViewById(R.id.recommend_endPlace_tv);
            tv_go_time=itemView.findViewById(R.id.recommend_location);
            tv_master=itemView.findViewById(R.id.recommend_master_name);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=mInflater.inflate(R.layout.item_recommend_road,parent,false);
                final RoadViewHolder roadViewHolder=new RoadViewHolder(view);
                return roadViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (recommendDataBeans.getBriefTeamDTOS()!=null) {
            teamId.add(Integer.parseInt(recommendDataBeans.getBriefTeamDTOS().get(position).getId()));
            RoadViewHolder roadViewHolder = (RoadViewHolder) holder;
            String timeStr = (recommendDataBeans.getBriefTeamDTOS()
                    .get(position)
                    .getTargetTime());
            roadViewHolder.tv_go_time.setText(timeStr
                    .replace("T"," ")
                    .substring(0, (timeStr.length() >= 19 ? 19 : timeStr.length())));
            roadViewHolder.tv_endPlace.setText(recommendDataBeans.getBriefTeamDTOS().get(position).getDestinationName());
            roadViewHolder.tv_firstPlace.setText(recommendDataBeans.getBriefTeamDTOS().get(position).getOriginName());
            roadViewHolder.tv_master.setText(recommendDataBeans.getBriefTeamDTOS().get(position).getLeaderName());

            roadViewHolder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recommendOnClick.joinRoute(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recommendDataBeans.getBriefTeamDTOS().size();
    }
}
