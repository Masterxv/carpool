package com.campussay.carpool.ui.recommend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * creat by teng on 2019/4/10
 */
public class RecommendArrowView extends View {
    // 设置画笔变量
    Paint mPaint = new Paint();

    // 自定义View有四个构造函数
    // 如果View是在Java代码里面new的，则调用第一个构造函数
    public RecommendArrowView(Context context) {
        super(context);
        // 在构造函数里初始化画笔的操作
        init();
    }


    // 如果View是在.xml里声明的，则调用第二个构造函数
    // 自定义属性是从AttributeSet参数传进来的
    public RecommendArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    // 画笔初始化
    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);//填充

        // 设置画笔颜色
        mPaint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine
                (0,15,90,15,mPaint);
        canvas.drawLine
                (90,15,50,0,mPaint);
        super.onDraw(canvas);
    }

}
