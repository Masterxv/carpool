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
import com.campussay.carpool.ui.chat.other.ChatMessage;
import com.campussay.carpool.ui.chat.other.MType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * create by zuyuan on 2019/4/9
 */
public class ChatRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> mMsg;

    private LayoutInflater mInflater;

    private SimpleDateFormat mDateFormat ;

    public ChatRecycleAdapter(List<ChatMessage> list) {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (list != null) {
            mMsg = list;
        } else {
            mMsg = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(CarpoolApplication.getApplication());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MType type = MType.get(viewType);
        RecyclerView.ViewHolder holder = null;
        View view = null;

        switch (type) {
            case TYPE_LEFT_TEXT:
                view = mInflater.inflate(R.layout.item_chat_left, parent, false);
                holder = new ChatRecycleAdapter.LeftTextHolder(view);
                break;
            case TYPE_RIGHT_TEXT:
                view = mInflater.inflate(R.layout.item_chat_right, parent, false);
                holder = new ChatRecycleAdapter.RightTextHolder(view);
                break;
            case TYPE_LEFT_VOICE:
                view = mInflater.inflate(R.layout.item_chat_left_voice, parent, false);
                holder = new ChatRecycleAdapter.LeftVoiceHolder(view);
                break;
            case TYPE_RIGHT_VOICE:
                view = mInflater.inflate(R.layout.item_chat_right_voice, parent, false);
                holder = new ChatRecycleAdapter.RightVoiceHolder(view);
                break;
            case TYPE_TIME:
                view = mInflater.inflate(R.layout.item_chat_time, parent, false);
                holder = new ChatRecycleAdapter.TimeHolder(view);
                break;
            case TYPE_REFRESH:
                view = mInflater.inflate(R.layout.item_chat_refresh, parent, false);
                holder = new ChatRecycleAdapter.RefreshHolder(view);
                break;
            default:
                throw new RuntimeException("not found message type! (enum MType.NULL)");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MType t = MType.get(getItemViewType(position));
        ChatMessage m = null;
        if (position >= 1) {
            m = mMsg.get(position - 1);
        }
        switch (t) {
            case TYPE_REFRESH:
                //取消RecyclerView的刷新Item 使用外部刷新
                break;
            case TYPE_LEFT_TEXT:
                LeftTextHolder lth = (LeftTextHolder) holder;
                lth.tvContent.setText(m.text);
                loadImage(m.headUrl, lth.ivAccount);
                break;
            case TYPE_RIGHT_TEXT:
                RightTextHolder rth = (RightTextHolder) holder;
                rth.tvContent.setText(m.text);
                if (m.failInSend && rth.tvFail.getVisibility() == View.GONE) {
                    rth.tvFail.setVisibility(View.VISIBLE);
                } else if (!m.failInSend && rth.tvFail.getVisibility() == View.VISIBLE){
                    rth.tvFail.setVisibility(View.GONE);
                }
                loadImage(m.headUrl, rth.ivAccount);
                break;
            case TYPE_TIME:
                TimeHolder th = (TimeHolder) holder;
                th.tvData.setText(parseTime(m.date));
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mMsg.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MType.TYPE_REFRESH.intValue;
        } else {
            return mMsg.get(position - 1).type.intValue;
        }
    }

    public void addBeforeMessage(List<ChatMessage> messages) {
        mMsg.addAll(0, messages);
    }

    public void addMessage(ChatMessage message) {
        mMsg.add(message);
    }

    public void addMessage(List<ChatMessage> messages) {
        mMsg.addAll(messages);
    }

    public int getListSize() {
        return mMsg.size();
    }

    public List<ChatMessage> getMsgs() {
        return mMsg;
    }

    public void recycleMsg() {
        for (ChatMessage m : mMsg) {
            m.recycle();
        }
        mMsg.clear();
    }

    /**
     * bug: 月份少了1
     */
    private String parseTime(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final int nowMoth = calendar.get(Calendar.MONTH) + 1;
        final int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(date);
        final int oldMonth = calendar.get(Calendar.MONTH) + 1;
        final int oldDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int oldHours = calendar.get(Calendar.HOUR_OF_DAY);
        final int oldMinutes = calendar.get(Calendar.MINUTE);

        StringBuilder builder = new StringBuilder();
        if (nowMoth - oldMonth != 0) {
            builder.append(oldMonth).append("月");
            builder.append(oldDay).append("日 ");
        } else if (nowDay - oldDay!= 0) {
            if (nowDay - oldDay == 1) {
                builder.append("昨天 ");
            } else if (nowDay - oldDay == 2) {
                builder.append("前天 ");
            } else {
                builder.append(oldMonth).append("月");
                builder.append(oldDay).append("日 ");
            }
        } else {
            builder.append("今天 ");
        }
        builder.append(oldHours < 10 ? ("0" + oldHours) : oldHours)
                .append(":")
                .append(oldMinutes < 10 ? ("0" +oldMinutes) : oldMinutes);
        return builder.toString();
    }

    private void loadImage(String url, ImageView iv) {
        Glide.with(CarpoolApplication.getApplication())
                .load(url)
                .error(Glide.with(CarpoolApplication.getApplication()).load(R.drawable.ic_fail2))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv);
    }

    static class LeftTextHolder extends RecyclerView.ViewHolder {
        ImageView ivAccount;

        TextView tvContent;

        public LeftTextHolder(View itemView) {
            super(itemView);
            ivAccount = itemView.findViewById(R.id.iv_item_chat_left_account);
            tvContent = itemView.findViewById(R.id.tv_item_chat_left_text);
        }
    }

    static class RightTextHolder extends RecyclerView.ViewHolder {

        ImageView ivAccount;

        TextView tvContent;

        TextView tvFail;

        public RightTextHolder(View itemView) {
            super(itemView);
            ivAccount = itemView.findViewById(R.id.iv_item_chat_right_account);
            tvContent = itemView.findViewById(R.id.tv_item_chat_right_text);
            tvFail = itemView.findViewById(R.id.tv_item_chat_right_fail);
        }
    }

    static class TimeHolder extends RecyclerView.ViewHolder {

        TextView tvData;

        public TimeHolder(View itemView) {
            super(itemView);
            tvData = (TextView) itemView;
        }
    }

    static class LeftVoiceHolder extends RecyclerView.ViewHolder {

        ImageView ivAccount;

        ImageView ivVoice;

        public LeftVoiceHolder(View itemView) {
            super(itemView);
            ivVoice = itemView.findViewById(R.id.iv_item_chat_left_voice);
            ivAccount = itemView.findViewById(R.id.iv_item_chat_left_voice_a);
        }
    }

    static class RightVoiceHolder extends RecyclerView.ViewHolder {

        ImageView ivAccount;

        ImageView ivVoice;

        public RightVoiceHolder(View itemView) {
            super(itemView);
            ivVoice = itemView.findViewById(R.id.iv_item_chat_right_voice);
            ivAccount = itemView.findViewById(R.id.iv_item_chat_right_voice_a);
        }
    }

    static class RefreshHolder extends RecyclerView.ViewHolder {

        TextView ivRefresh;

        public RefreshHolder(View itemView) {
            super(itemView);
            ivRefresh = (TextView) itemView;
        }
    }
}
