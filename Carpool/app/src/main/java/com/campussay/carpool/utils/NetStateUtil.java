package com.campussay.carpool.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * create by WenJinG on 2019/4/4
 */
public class NetStateUtil {

    //判断网络是否连接
    public static boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager mManager = (ConnectivityManager)context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mManager.getActiveNetworkInfo();
            if(mNetworkInfo != null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断是否是移动网络
    public static  boolean isMobileNetworkConnected(Context context){

        if(context != null){
            ConnectivityManager mManager = (ConnectivityManager)context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mManager.getActiveNetworkInfo();
            return mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    //判断是否是wifi网络
    public static  boolean isWIFINetworkConnected(Context context){

        if(context != null){
            ConnectivityManager mManager = (ConnectivityManager)context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mManager.getActiveNetworkInfo();
            return mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    //获取网络运营商信息
    public static String OparetorOfNetwork(Context context){
        String Msg = "UnKnow";
        TelephonyManager mTelephonyManager = (TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if(isNetworkConnected(context)){
            return Msg = mTelephonyManager.getNetworkOperatorName();
        }
        return Msg;
    }
}
