package com.campussay.carpool.utils;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * create by WenJinG on 2019/4/27
 */
public class BuglyUtil {

    private static String TAG= "BuglyLog";


    public static void e(String s){
        BuglyLog.e(TAG,s);
    }

    public static void d(String s){
        BuglyLog.d(TAG,s);
    }

    public static void updateThrowable(Throwable e){
        CrashReport.postCatchedException(e);
    }
}
