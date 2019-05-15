package com.campussay.carpool.ui.login;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.campussay.carpool.R;
import com.campussay.carpool.ui.base.BaseActivity;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.utils.Constants;
import com.campussay.carpool.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class LoginActivity extends BaseActivity {

    private Button btnLogin;

    private AlertDialog mDialog;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        WXEntryActivity.mLoginActivity = this;
        //配置好微信登录
        Constants.wx_api = WXAPIFactory.createWXAPI(LoginActivity.this, Constants.APP_ID, true);
        Constants.wx_api.registerApp(Constants.APP_ID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";

        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> {
            showLoadDialog();
            Constants.wx_api.sendReq(req);
        });
    }

    private void showLoadDialog() {
        View v = LayoutInflater.from(this)
                .inflate(R.layout.dialog_load, null ,false);
        mDialog = new AlertDialog.Builder(this, R.style.loadDialogTheme)
                .setView(v)
                .setCancelable(true)
                .create();
        ImageView iv = v.findViewById(R.id.c_iv_dialog_load);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_load)
                .into(iv);
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }
}
