package com.campussay.carpool.ui.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.campussay.carpool.R;
import com.campussay.carpool.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

/**
 * create by WenJinG on 2019/4/8
 */
public class PostInfoPicker extends FrameLayout implements NumberPicker.OnValueChangeListener{
    private View mView;
    private PostNumberPicker month;
    private PostNumberPicker day;
    private PostNumberPicker hour;
    private PostNumberPicker minute;
    private int monthValue;
    private int dayValue;
    private int hourValue;
    private int minuteValue;
    private int peopleValue = 2;
    private Calendar date;
    private onInfoChangeListener mListener;


    public PostInfoPicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mView  = LayoutInflater.from(context).inflate(R.layout.post_infopicker,this,true);

        date = Calendar.getInstance();
        date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        month = mView.findViewById(R.id.post_month);
        month.setFormatter(value -> value+"月");
        month.setMinValue(1);
        month.setMaxValue(12);
        month.setValue( monthValue=date.get(Calendar.MONTH)+1);
        month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        month.setOnValueChangedListener(this);
        month.setWrapSelectorWheel(false);

        day = mView.findViewById(R.id.post_day);
        day.setFormatter(value -> value+"日");
        day.setMinValue(1);
        day.setMaxValue(31);
        day.setValue(date.get(dayValue=Calendar.DAY_OF_MONTH));
        day.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        day.setOnValueChangedListener(this);
        day.setWrapSelectorWheel(false);

        hour = mView.findViewById(R.id.post_hour);
        hour.setFormatter(value -> value+"点");
        hour.setMinValue(0);
        hour.setMaxValue(23);
        hour.setValue(hourValue=date.get(Calendar.HOUR_OF_DAY));
        hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hour.setOnValueChangedListener(this);
        hour.setWrapSelectorWheel(false);

        minute = mView.findViewById(R.id.post_minute);
        minute.setFormatter(value -> value+"分");
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setValue(minuteValue=date.get(Calendar.MINUTE));
        minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minute.setOnValueChangedListener(this);
        minute.setWrapSelectorWheel(false);

    }

    public PostInfoPicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public PostInfoPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PostInfoPicker(@NonNull Context context) {
        this(context,null);

    }


    public String getTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
        String dateStr = df.format(date.getTime());
        return dateStr;
    }

    private String getPeopleNumber(){
        return "拼"+peopleValue+"人"+"的队伍";
    }

    public void setonInfoChangeListener(onInfoChangeListener mCallback) {
        this.mListener = mCallback;
    }

    private void onInfoChange(){
        LogUtils.d("数据改变，即将回调了");
        if(mListener!=null){
            mListener.onInfoChange(date,getPeopleNumber());
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.post_month:
                LogUtils.d(newVal+"数据改变，即将回调了");
                monthValue = newVal;

                date.set(Calendar.MONTH,monthValue-1);

                onInfoChange();
                break;
            case R.id.post_minute:
                minuteValue = newVal;
                date.set(Calendar.MINUTE,minuteValue);
                onInfoChange();
                break;
            case R.id.post_day:
                dayValue = newVal;
                date.set(Calendar.DAY_OF_MONTH,dayValue);
                onInfoChange();
                break;
            case R.id.post_hour:
                hourValue = newVal;
                date.set(Calendar.HOUR_OF_DAY,hourValue);
                onInfoChange();
                break;
        }
    }

    interface onInfoChangeListener{
        void onInfoChange(Calendar calendar,String peopleNumber);
    }

}
