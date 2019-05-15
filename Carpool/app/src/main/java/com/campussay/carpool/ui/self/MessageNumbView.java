package com.campussay.carpool.ui.self;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.campussay.carpool.R;

/**
 * Create by Zhangfan on 2019/4/11
 * 图片左上角显示消息数
 **/
public class MessageNumbView extends View {
    private Paint paint = null;//画背景的画笔
    private Paint textPaint = null;//画数字
    private int circle_color;
    private int text_color;
    private boolean isShow;//是否显示圆圈消息
    private String text;
    private Bitmap bitmap;
    private int rate=6;//圆和正方形的比例
    private int square_color;//正方形的颜色


    public MessageNumbView(Context context) {
        this(context,null);
    }

    public MessageNumbView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public MessageNumbView(Context context,AttributeSet attrs,int i){
        super(context, attrs,i);
        init();
    }

    public synchronized void setText(String text){
        this.text = text;
        init();
        invalidate();
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        init();
        invalidate();
    }
    public synchronized void setShow(boolean isShow){
        this.isShow = isShow;
        init();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = Math.min(getWidth(),getHeight());
        int h = Math.min(getWidth(),getHeight());

        //获取文字的高度和宽度
        Rect rect = new Rect();
        textPaint.setColor(text_color);
        paint.setColor(circle_color);
        textPaint.setTextSize(30);
        textPaint.setFakeBoldText(true);
        textPaint.getTextBounds(text,0,text.length(),rect);

        //获取指定图片
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        //已知图片的尺寸，根据比例获取圆的半径
        int r = bitmap.getWidth() / rate;//圆的半径
        int c = bitmap.getWidth();//正方形周长

        //判断文字和圆的半径的大小关系，如果文字长度比圆半径大，那么就需要改变圆的半径
        int x = rect.centerX() - r;
        int y = rect.centerY() - r;
        if (x > 0 || y > 0 ){
            r = (int) Math.sqrt(x*x + y*y);
        }
        //画图形
        canvas.drawBitmap(bitmap,0,r,paint);

        if(isShow){//如果需要展示图标
            drawCircle(canvas, r, c);//画圆圈
            drawText(canvas, r, c,rect);//画文本
        }

    }

    private void drawText(Canvas canvas,int r, int c,Rect rect) {
        int x = c-rect.centerX();
        int y = r - rect.centerY();
        canvas.drawText(text,x,y, textPaint);
    }

    private void drawCircle(Canvas canvas, int r, int c) {
        paint.setColor(circle_color);
        canvas.drawCircle(c,r,r,paint);
    }

    /*
    * 初始化操作
    * */
    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circle_color = getResources().getColor(R.color.message_numb_background);
        text_color = getResources().getColor(R.color.post_button_unpressed);
    }
}
