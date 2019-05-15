package com.campussay.carpool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Create by Zhangfan on 2019/4/18
 **/
public class GrayBitmapTransfer extends BitmapTransformation {
    private RenderScript rs;
    public GrayBitmapTransfer(Context context) {

        rs = RenderScript.create( context );
    }
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = null;
        //获取图片的宽和高
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
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
        canvas.drawBitmap(toTransform, 0,0, paint);
        return bitmap;


    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
