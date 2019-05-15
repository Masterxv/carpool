package com.campussay.carpool.xg;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.util.Log;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.R;
import com.campussay.carpool.utils.LogUtils;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGLocalMessage;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * create by zuyuan on 2019/4/15
 * 信鸽推送消息接收器
 */
public class XGMessageReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i,
                                 XGPushRegisterResult xgPushRegisterResult) {
        //注册的结果
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        //反注册的结果
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        //标签信息
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        //删除标签信息
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        //应用内消息
        XGMessageBean bean = parse(message.getContent());
        if (bean != null && bean.isSuccess()) {
            showNotification(getNotificationTitle(bean),
                    bean.getData().getContent());
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult xgPushClickedResult) {
        //暂不处理
    }

    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult xgPushShowedResult) {
    }

    private void showNotification(String title, String text) {
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(RingtoneManager.getActualDefaultRingtoneUri(
                CarpoolApplication.getApplication(), RingtoneManager.TYPE_ALARM)) // 设置声音
                //.setDefaults(Notification.DEFAULT_VIBRATE) // 振动
                .setFlags(Notification.FLAG_AUTO_CANCEL); // 优先级
        build.setLayoutIconId(R.id.iv_notification);
        build.setLayoutId(R.layout.layout_notification);
        build.setLayoutTextId(R.id.tv_notification_text);
        build.setLayoutTitleId(R.id.tv_notification_title);
        build.setLayoutTimeId(R.id.tv_notification_time);

        build.setIcon(R.drawable.ic_small_logo);
        build.setWhen(System.currentTimeMillis());
        build.setLayoutIconDrawableId(R.drawable.ic_small_logo);
        build.setNotificationLargeIcon(R.drawable.ic_small_logo);

        XGLocalMessage localMessage = new XGLocalMessage();
        localMessage.setTitle(title);
        localMessage.setContent(text);

        XGPushManager.setDefaultNotificationBuilder(CarpoolApplication.getApplication(), build);
        XGPushManager.addLocalNotification(CarpoolApplication.getApplication(), localMessage);
    }

    public static String getNotificationTitle(XGMessageBean bean) {
        StringBuilder builder = new StringBuilder();
        XGMessageBean.DataBean data = bean.getData();
        switch (data.getType()) {
            case 0: //申请加入
                builder.append(data.getWho())
                        .append(" ")
                        .append("申请加入队伍: ");
                break;
            case 1: //申请通过
                builder.append(data.getWho())
                        .append(" ")
                        .append("同意你加入队伍: ");
                break;
            case 2: //私聊
                builder.append(data.getWho())
                        .append(" 发来新消息: ");
                break;
            case 3: //群聊
                builder.append(data.getWho())
                        .append(" 有新消息: ");
                break;
            case 4: //成为新队长
                builder.append("你已成为以下队伍的队长: ");
            default:
                builder.append(" ");
                break;
        }
        return builder.toString();
    }

    public XGMessageBean parse(String gStr) {
        if (gStr == null) return null;

        int startIndex = 1;
        int endIndex = gStr.length() - 1;

        String targetStr = gStr.substring(startIndex, endIndex)
                .replace("\\", "");

        Gson g = new Gson();
        return g.fromJson(targetStr, XGMessageBean.class);
    }
}
