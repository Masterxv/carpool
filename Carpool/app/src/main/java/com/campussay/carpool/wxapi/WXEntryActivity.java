package com.campussay.carpool.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.campussay.carpool.ShellActivity;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.ui.login.LoginActivity;
import com.campussay.carpool.utils.Constants;
import com.campussay.carpool.utils.LogUtils;
import com.campussay.carpool.utils.LoginUtil;
import com.campussay.carpool.utils.WXAccessTokenEntity;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * creat by teng on 2019/4/4
 */

/**
 * //创建微信api并注册到微信
 * Constants.wx_api = WXAPIFactory.createWXAPI(MainActivity.this, Constants.APP_ID, true);
 * Constants.wx_api.registerApp(Constants.APP_ID);
 * <p>
 * 登录的时候:
 * final SendAuth.Req req = new SendAuth.Req();
 * req.scope = "snsapi_userinfo";
 * req.state = "wechat_sdk_demo_test";
 * Constants.wx_api.sendReq(req);
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    int times = 1;

    public static LoginActivity mLoginActivity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //通过WXAPIFactory工厂获取IWXApI的示例
        Constants.wx_api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        //将应用的appid注册到微信
        Constants.wx_api.registerApp(Constants.APP_ID);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false
        //则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = Constants.wx_api.handleIntent(getIntent(), this);

            if (!result) {
                LogUtils.d("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean result = Constants.wx_api.handleIntent(getIntent(), this);

    }

    //微信请求相应
    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.d("onReq");

    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        LogUtils.d("onResp");

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                toLogin(resp);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                LogUtils.i("onResp ERR_USER_CANCEL ");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                LogUtils.i("onResp ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                LogUtils.i("onResp default errCode " + resp.errCode);
                //发送返回
                break;
        }
        finish();
    }

    private void toLogin(BaseResp resp) {
        LogUtils.d( "微信登录成功");

        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
            // getAccessToken(code);
            times++;
            if (times == 2) {
                requestForToken(code);
            }
            LogUtils.i("onResp code = " + code);
        }
    }

    private void requestForToken(String code) {
        AppNetClient.getInstance()
                .login(code)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }


                    @Override
                    public void onNext(LoginBean b) {
                        if (b.isSuccess()) {
                            LoginUtil.save(WXEntryActivity.this, b);
                            Intent intent = new Intent(WXEntryActivity.this,
                                    ShellActivity.class);
                            intent.putExtra("login", true);
                            startActivity(intent);
                            if (mLoginActivity != null) mLoginActivity.finish();
                        } else {
                            if (b.getCode() == 4000)
                                Toast.makeText(WXEntryActivity.this, "登录已过期",
                                    Toast.LENGTH_SHORT).show();
                            else if (b.getCode() == 1)
                                Toast.makeText(WXEntryActivity.this, "登录错误",
                                        Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(WXEntryActivity.this, "登录失败",
                                        Toast.LENGTH_SHORT).show();
                            if (mLoginActivity != null) mLoginActivity.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(WXEntryActivity.this, "登录失败",
                                Toast.LENGTH_SHORT).show();
                        if (mLoginActivity != null)
                            mLoginActivity.dismiss();
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    public void getAccessToken(String code) {
        final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constants.APP_ID
                + "&secret="
                + Constants.APP_SECRET_WX
                + "&code="
                + code
                + "&grant_type=authorization_code";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String access_token = response.body().string();
                    Log.d("TAG", access_token);

                    Gson gson = new Gson();
                    WXAccessTokenEntity wxAccessTokenEntity = gson.fromJson(access_token, WXAccessTokenEntity.class);
                    String opendid = wxAccessTokenEntity.getOpenid();
                    Log.d("TAG", "opendid+" + opendid);
                    WXData.token = wxAccessTokenEntity.getAccess_token();

                    String m = wxAccessTokenEntity.getOpenid();
                    String p = wxAccessTokenEntity.getUnionid();
                    String i = wxAccessTokenEntity.getRefresh_token();
                    String b = wxAccessTokenEntity.getScope();
                    Log.d("TAGG", m + "     " + p + "      " + WXData.token + "       " + i + "       " + b);
                    //WXData.id=wxAccessTokenEntity.

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}