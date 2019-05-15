package com.campussay.carpool.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.route.bean.ApplyBean;
import com.campussay.carpool.utils.DateUtil;
import com.campussay.carpool.utils.GrayBitmapTransfer;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Create by Zhangfan on 2019/4/13
 **/
public class RouteApplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ApplyBean.Data> data;
    private Context mContext;
    private LayoutInflater inflater;
    private static final int TYPE_CHECKED = 2;//审核通过
    private static final int TYPE_CHECKING = 1;//正在审核中
    private static final int TYPE_FAIL = 0;//审核未通过

    private static final int LOAD_MORE = 4;

    private OnLoadMoreClickListener mMoreClickListener;

    private LoadMoreHolder mMoreHolder ;

    @Override
    public int getItemViewType(int position) {
        if (position == (data == null ? 0 : data.size())) return LOAD_MORE;
        return data.get(position).getType();
    }

    public RouteApplyAdapter(List<ApplyBean.Data> data, Context context){
        this.data = data;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        mMoreHolder = new LoadMoreHolder(
                inflater.inflate(R.layout.item_apply_bottom, null, false));
    }

    public void setList(List<ApplyBean.Data> list){
        this.data = list;
        notifyDataSetChanged();
    }

    public void addList(List<ApplyBean.Data> list) {
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        if (!hasMore) {
            mMoreHolder.tvLoadMore.setText("没有更多");
            if (mMoreHolder.ivDown.getVisibility() == View.VISIBLE)
                mMoreHolder.ivDown.setVisibility(View.INVISIBLE);
            mMoreHolder.tvLoadMore.setOnClickListener(v -> {});
        } else {
            mMoreHolder.tvLoadMore.setText("显示更多");
            if (mMoreHolder.ivDown.getVisibility() == View.INVISIBLE)
                mMoreHolder.ivDown.setVisibility(View.VISIBLE);
            mMoreHolder.tvLoadMore.setOnClickListener(v -> {
                if (mMoreClickListener != null) mMoreClickListener.onClick(v);
            });
        }
    }

    /*
    * Glide灰度处理
    * */
    private void glideGrayBitmap(String url,ImageView imageView){
        Glide.with(mContext)
                .load(url)
                .error(Glide.with(mContext).load(R.drawable.ic_fail2))
                .apply(RequestOptions.bitmapTransform(new GrayBitmapTransfer(mContext)))
                .into(imageView);
    }

    private void glideBitmap(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .error(Glide.with(mContext).load(R.drawable.ic_fail2))
                .into(imageView);
    }

    /*
    * 图片灰度处理
    * */

    public static Bitmap getGrayBitmap(Bitmap bm){
        Bitmap bitmap = null;
        //获取图片的宽和高
        int width = bm.getWidth();
        int height = bm.getHeight();
        //创建灰度图片
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //创建画布
        Canvas canvas = new Canvas(bitmap);
        //创建画笔
        Paint paint = new Paint();
        //创建颜色矩阵
        ColorMatrix matrix = new ColorMatrix();
        //设置颜色矩阵的饱和度:0代表灰色,1表示原图
        matrix.setSaturation(0);
        //颜色过滤器
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);
        //设置画笔颜色过滤器
        paint.setColorFilter(cmcf);
        //画图
        canvas.drawBitmap(bm, 0,0, paint);
        return bitmap;
    }


    //已经审核过得item
    static class CheckedHolder extends RecyclerView.ViewHolder{
        TextView tv_start,tv_end,tv_result,tv_date,tv_name;
        ImageView iv_head, iv_top_right, iv_jiantou;
        public CheckedHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.apply_item_checked_date);
            tv_end = itemView.findViewById(R.id.apply_item_checked_end);
            tv_result = itemView.findViewById(R.id.apply_item_checked_result);
            tv_start = itemView.findViewById(R.id.apply_item_checked_start);
            tv_name = itemView.findViewById(R.id.apply_item_tv_name);
            iv_head = itemView.findViewById(R.id.apply_item_checked_head);
            iv_top_right = itemView.findViewById(R.id.apply_item_iv_top_right);
            iv_jiantou = itemView.findViewById(R.id.arrow_view);
        }
    }

    static class LoadMoreHolder extends RecyclerView.ViewHolder {
        TextView tvLoadMore;
        ImageView ivDown;
        public LoadMoreHolder(View itemView) {
            super(itemView);
            tvLoadMore = itemView.findViewById(R.id.route_apply_tv);
            ivDown = itemView.findViewById(R.id.route_apply_iv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE)
            return mMoreHolder;

        RecyclerView.ViewHolder holder = null;
        View view = inflater.inflate(R.layout.route_apply_item_checked,null, false);
        holder = new CheckedHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != LOAD_MORE) {
            CheckedHolder checkedHolder = (CheckedHolder) holder;
            checkedHolder.tv_start.setText(data.get(position).getOrigin());
            checkedHolder.tv_end.setText(data.get(position).getDestination());
            checkedHolder.tv_date.setText(
                    new SimpleDateFormat("yyyy/MM/dd E HH:mm")
                            .format(Double.parseDouble(data.get(position).getTargetTime())));
            if (data.get(position).getLeaderInfo() != null) {
                checkedHolder.tv_name.setText(data.get(position).getLeaderInfo().getName());
            }
        }

        switch (getItemViewType(position))  {
            case TYPE_FAIL:
                CheckedHolder checkedHolder1 = (CheckedHolder) holder;
                checkedHolder1.tv_result.setText("未同意");
                checkedHolder1.tv_end.setTextColor(Color.parseColor("#dddddd"));
                checkedHolder1.tv_start.setTextColor(Color.parseColor("#dddddd"));
                checkedHolder1.tv_date.setTextColor(Color.parseColor("#dddddd"));
                checkedHolder1.tv_result.setTextColor(Color.parseColor("#dddddd"));
                checkedHolder1.tv_name.setTextColor(Color.parseColor("#dddddd"));
                if (data.get(position).getLeaderInfo() != null) {
                    glideGrayBitmap(data.get(position).getLeaderInfo().getPhoto(),
                            checkedHolder1.iv_head);//灰度图片
                }
                checkedHolder1.iv_top_right.setImageDrawable(mContext.getDrawable(R.drawable.img_top_right2));
                checkedHolder1.iv_jiantou.setImageDrawable(mContext.getDrawable(R.drawable.ic_jiantou_apply2));
                break;
            case TYPE_CHECKING:
                CheckedHolder checkedHolder2 = (CheckedHolder) holder;
                checkedHolder2.tv_result.setText("待同意");
                checkedHolder2.tv_result.setTextColor(Color.parseColor("#969696"));
                if (data.get(position).getLeaderInfo() != null) {
                    glideBitmap(data.get(position).getLeaderInfo().getPhoto(),
                            checkedHolder2.iv_head);
                }
                checkedHolder2.iv_top_right.setImageDrawable(mContext.getDrawable(R.drawable.img_tanslate));
                break;
            case TYPE_CHECKED:
                CheckedHolder checkedHolder3 = (CheckedHolder) holder;
                checkedHolder3.tv_result.setText("已同意");
                if (data.get(position).getLeaderInfo() != null) {
                    glideBitmap(data.get(position).getLeaderInfo().getPhoto(), checkedHolder3.iv_head);
                }
                checkedHolder3.iv_top_right.setImageDrawable(mContext.getDrawable(R.drawable.img_top_right1));
                break;
            case LOAD_MORE:
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 1 : data.size() + 1;
    }

    public interface OnLoadMoreClickListener {
        void onClick(View v);
    }

    public void addLoadMoreClickListener(OnLoadMoreClickListener listener) {
        mMoreClickListener = listener;
    }
}
