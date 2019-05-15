package com.campussay.carpool.utils;

import android.util.Log;

/**
 * create by WenJinG on 2019/4/5
 */
public class LogUtils {
    //true为开启日志打印，false为关闭日志打印
    public static boolean debug = true;
    public static final String sign = "TAG----------";

    public static void e(String s){
        if(debug)
        Log.e(sign,s);
    }

    public static void d(String s){
        if(debug)
        Log.d(sign,s);
    }

    public static void i(String s){
        if(debug)
        Log.i(sign,s);
    }

    public static void v(String s){
        if(debug)
        Log.v(sign,s);
    }

    public static void w(String s){
        if(debug)
        Log.w(sign,s);
    }

    public static void wtf(String s){
        if(debug)
        Log.wtf(sign,s);
    }
}
