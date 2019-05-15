package com.campussay.carpool;

import android.app.Application;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.campussay.carpool.utils.LogUtils;
import com.campussay.carpool.utils.ScreenAttrUtil;
import com.campussay.carpool.xg.XGManagerUtil;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * create by WenJinG on 2019/4/4
 */
public class CarpoolApplication extends Application {

    private static CarpoolApplication application;

    private volatile static Account mAccount;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        saveScreenAttr();

        //配置信鸽
        XGManagerUtil.config(this);

        //配置bugly
        CrashReport.initCrashReport(getApplicationContext(), "a1ca967d8", true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //解绑
        if (mAccount != null)
            XGManagerUtil.unBindAccount(this, String.valueOf(mAccount.id));
        XGManagerUtil.finish(this);
    }

    public static Application getApplication() {
            return application;
    }

    public static Account getAccount() {
        return mAccount;
    }

    public static void setAccount(Account account) {
        //解绑之前的账户，绑定现在的账户
        if (mAccount != null ) {
            if (mAccount.getId() != account.getId()) {
                XGManagerUtil.unBindAccount(CarpoolApplication.getApplication(),
                        String.valueOf(mAccount.id));
                XGManagerUtil.bindAccount(CarpoolApplication.getApplication(),
                        String.valueOf(account.id));
            }
        } else {
            XGManagerUtil.bindAccount(CarpoolApplication.getApplication(),
                    String.valueOf(account.id));
        }
        mAccount = account;
    }


    /**
     * 初始保存屏幕信息
     */
    private void saveScreenAttr() {
        ScreenAttrUtil.initAttr(this);
    }

    public static class Account {
        private final int id ;
        private final String token;
        private final String name;
        private final String headUrl;
        private final int gender;


        public Account(int id, String token, String name, String headUrl, int gender) {
            this.id = id;
            this.name = name;
            this.headUrl = headUrl;
            this.gender = gender;
            this.token = token;
        }

        public int getId() {
            return id;
        }

        public String getToken() {
            return token;
        }

        public String getName() {
            return name;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public int getGender() {
            return gender;
        }
    }
}

