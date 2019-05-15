package com.campussay.carpool.xg;

import android.content.Context;
import android.util.Log;

import com.campussay.carpool.utils.LogUtils;
import com.campussay.carpool.utils.LoginUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

/**
 * create by zuyuan on 2019/4/15
 */
public class XGManagerUtil {

    /**
     * 为当前设备配置、注册好信鸽
     * 此处获取信鸽给的token
     */
    public static void config(Context context) {
        XGPushConfig.setAccessId(context, 2100331549);
        XGPushConfig.setAccessKey(context, "A9L3H7VA9P6Z");
        XGPushConfig.enableDebug(context, true);
        XGPushManager.registerPush(context, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
            }

            @Override
            public void onFail(Object o, int i, String s) {
            }
        });
    }

    /**
     * 为当前设备(token)绑定账号
     * @param account 用户登录之后的id
     */
    public static void bindAccount(Context context, String account) {
        XGPushManager.bindAccount(context, account, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
            }

            @Override
            public void onFail(Object o, int i, String s) {
            }
        });
    }

    /**
     * 退出程序、注销账号时应该调用
     * @param account 用户登录之后的id
     */
    public static void unBindAccount(Context context, String account) {
        XGPushManager.delAccount(context, account);
    }

    /**
     * 退出程序时调用
     */
    public static void finish(Context context) {
        XGPushManager.unregisterPush(context);
    }
}
