package com.campussay.carpool.ui.post;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.campussay.carpool.utils.ScreenAttrUtil;

/**
 * create by WenJinG on 2019/4/11
 */
public class PostNumberPicker extends NumberPicker {
    public PostNumberPicker(Context context) {
        super(context);
    }

    public PostNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        alterTextSize(child);
    }


    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        alterTextSize(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        alterTextSize(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        alterTextSize(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        alterTextSize(child);
    }

    private void alterTextSize(View view) {
        if (view instanceof EditText) {
            //这里修改字体的属性
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, 14 * ScreenAttrUtil.getDensity());
        }
    }
}


