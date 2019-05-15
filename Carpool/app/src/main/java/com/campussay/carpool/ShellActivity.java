package com.campussay.carpool;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.campussay.carpool.ui.CarpoolViewPager;
import com.campussay.carpool.ui.adapter.CarpoolViewPagerAdapter;
import com.campussay.carpool.ui.base.BaseActivity;
import com.campussay.carpool.ui.login.LoginActivity;
import com.campussay.carpool.utils.Location;
import com.campussay.carpool.utils.LogUtils;
import com.campussay.carpool.utils.LoginUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * create by WenJinG on 2019/4/6
 */
public class ShellActivity extends BaseActivity<ShellPresenter> {
    private TabLayout mTabLayout;
    private PressBackListener backListener = null;
    private long lastTime = 0;
    private Location location;
    private BigDecimal mLongitude;
    private BigDecimal mLatitude;
    private String mTitle ;
    private String mCity;
    private List<LocationChangeListener> mLocationChangeListener = new ArrayList<>();
    private AlertDialog mLoadDialog;

    public static final String[]tab = new String[]{"推荐","发布","行程","我的"};

    private boolean isLoginFirst = true;

    @Override
    protected void onPause() {
        super.onPause();
        if(location !=null){
            location.stopLocate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isLoginFirst = true;
        if(location !=null){
            location.destroyClient();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(location !=null && !isLoginFirst){
            location.startLocate();
        }
        if (!LoginUtil.isRunning()) {
            LoginUtil.login(this, new LoginUtil.CallBack() {
                @Override
                public void onSuccess() { }

                @Override
                public void onError(int code) {
                    loginError(code);
                }
            });
        }
    }

    @Override
    public ShellPresenter createPresenter() {
        return new ShellPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_shell;
    }

    @Override
    public void initView() {

        initTabLayout();
        setToolBarTitle("推荐");
        //若不是来自于登录界面则需要弹出正在登录加载框
       if (getIntent().getBooleanExtra("login", false))
           showLoadDialog();
        LoginUtil.login(this, new LoginUtil.CallBack() {
            @Override
            public void onSuccess() {
                LogUtils.w("初始化定位");
                requestPermission();
                //initLocation();
            }

            @Override
            public void onError(int code) {
                if (mLoadDialog != null && mLoadDialog.isShowing())
                    mLoadDialog.dismiss();
                loginError(code);
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        initLocation();
    }

    private void loginError(int code) {
        switch (code) {
            case LoginUtil.CallBack.FIRST:
                //Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
                break;
            case LoginUtil.CallBack.OVERDUE:
                Toast.makeText(this, "登录已过期", Toast.LENGTH_SHORT).show();
                break;
            case LoginUtil.CallBack.ERROR:
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
                break;
            case LoginUtil.CallBack.INTERNET_ERROR:
                Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
        startActivity(new Intent(ShellActivity.this, LoginActivity.class));
        finish();
    }


    private void initTabLayout(){
        mTabLayout = findViewById(R.id.tab);
        CarpoolViewPager viewPager= findViewById(R.id.carpoolViewpager);
        viewPager.setScanScroll(false);
        viewPager.setAdapter(new CarpoolViewPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(viewPager);
        for(int i =0;i<tab.length;i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            LogUtils.d((tab==null)+"");
            if(i==0){
                tab.setCustomView(setTabLayoutItem(R.drawable.viewpager_icon_recommend_press,"推荐"));

            }else if(i==1){
                tab.setCustomView(setTabLayoutItem(R.drawable.viewpager_icon_post_unpress,"发布"));
            }else if(i==2){
                tab.setCustomView(setTabLayoutItem(R.drawable.viewpager_icon_route_unpress,"行程"));
            }else {
                tab.setCustomView(setTabLayoutItem(R.drawable.viewpager_icon_self_unpress,"我的"));
            }
            LogUtils.d((tab.getCustomView()==null)+"自定义item为空");
        }

        mTabLayout.clearOnTabSelectedListeners();
        viewPager.setOffscreenPageLimit(1);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                onTabLayoutItemPress(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                onTabLayoutItemUnpress(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View setTabLayoutItem(int icon,String title){
        View item = LayoutInflater.from(this).inflate(R.layout.tablayout_item,null);
        ImageView imageView = item.findViewById(R.id.tabLayout_image);
        TextView textView = item.findViewById(R.id.tabLayout_text);
        imageView.setImageResource(icon);
        textView.setText(title);
        return item;
    }

    private void onTabLayoutItemPress(TabLayout.Tab tab){
        View item = tab.getCustomView();
        ImageView imageView = item.findViewById(R.id.tabLayout_image);
        if(tab == mTabLayout.getTabAt(0)){
            imageView.setImageResource(R.drawable.viewpager_icon_recommend_press);
            setToolBarTitle("推荐");
        }else if(tab == mTabLayout.getTabAt(1)){
            imageView.setImageResource(R.drawable.viewpager_icon_post_press);
            setToolBarTitle("发布");
        }else if(tab == mTabLayout.getTabAt(2)){
            imageView.setImageResource(R.drawable.viewpager_icon_route_press);
            setToolBarTitle("行程");
        }else {
            imageView.setImageResource(R.drawable.viewpager_icon_self_press);
            setToolBarTitle("我的");
        }
    }

    private void onTabLayoutItemUnpress(TabLayout.Tab tab){
        View item = tab.getCustomView();
        ImageView imageView = item.findViewById(R.id.tabLayout_image);
        if(tab == mTabLayout.getTabAt(0)){
            imageView.setImageResource(R.drawable.viewpager_icon_recommend_unpress);
        }else if(tab == mTabLayout.getTabAt(1)){
            imageView.setImageResource(R.drawable.viewpager_icon_post_unpress);
        }else if(tab == mTabLayout.getTabAt(2)){
            imageView.setImageResource(R.drawable.viewpager_icon_route_unpress);
        }else {
            imageView.setImageResource(R.drawable.viewpager_icon_self_unpress);
        }
    }

    public void initLocation(){
        location = Location.getInstance().configClient(this).setLocateListener(
                (latitude, longitude, cityName, title) -> {
                    if (mLoadDialog != null && mLoadDialog.isShowing())
                        mLoadDialog.dismiss();
            mLongitude = longitude;
            mLatitude = latitude;
            mTitle = title;
            mCity = cityName;
           if(mLocationChangeListener!=null){
               for(LocationChangeListener listener:mLocationChangeListener)
               listener.onLocationChange(mLatitude,mLongitude,mTitle,mCity);
           }
        });
    }

    public interface PressBackListener{
        boolean onBack();
    }

    public void setBackListener(PressBackListener backListener) {
        this.backListener = backListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(backListener!=null){
                if(!backListener.onBack() ){
                   return true;
                }
                if((System.currentTimeMillis() - lastTime) > 2000){
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    lastTime = System.currentTimeMillis();
                    return true;
                }
            }else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if(ShellActivity.this.getCurrentFocus()!=null)
            if (ShellActivity.this.getCurrentFocus().getWindowToken() != null) {
                if(imm!=null)
                imm.hideSoftInputFromWindow(ShellActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }

        }
        return super.onTouchEvent(event);
    }

    private void showLoadDialog() {
        View v = LayoutInflater.from(this)
                .inflate(R.layout.dialog_load, null ,false);
        mLoadDialog = new AlertDialog.Builder(this, R.style.loadDialogTheme)
                .setView(v)
                .setCancelable(false)
                .create();
        ImageView iv = v.findViewById(R.id.c_iv_dialog_load);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_load)
                .into(iv);
        mLoadDialog.show();
    }

    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未获取权限
            //只会展示一次
            LocationManager lm = (LocationManager) CarpoolApplication.getApplication()
                    .getSystemService(CarpoolApplication.getApplication().LOCATION_SERVICE);
            if(Build.VERSION.SDK_INT >=9.0) {
                if (lm != null)
                    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("请打开GPS连接")
                                .setMessage("为了提高定位的准确度，更好的为您服务，请打开GPS")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //跳转到手机打开GPS页面
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        //设置完成后返回原来的界面
                                        startActivityForResult(intent,0);
                                    }
                                })
                                .setNeutralButton("取消", (dialogInterface, i) -> {
                                    Toast.makeText(ShellActivity.this,
                                            "你没有开启定位，会影响你的体验",Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }).show();
                    }
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //当用户第一次申请拒绝时，再次申请该权限调用
                Toast.makeText(this, "打开定位权限", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {//未获取权限
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 0x01);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                    String deviceId = TelephonyMgr.getDeviceId();
                    CrashReport.setUserId(deviceId);
                }
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0x01);
        } else {
            initLocation();
        }

    }
    public void addLocationChangeListener(LocationChangeListener locationChangeListener) {
        mLocationChangeListener.add(locationChangeListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("result 回调了");
        if(requestCode == 0){
            LocationManager lm = (LocationManager) CarpoolApplication.getApplication()
                    .getSystemService(CarpoolApplication.getApplication().LOCATION_SERVICE);
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                initLocation();
            }else {
                Toast.makeText(this,"GPS未打开，请先打开GPS",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clearLocationChangeListener(){
        mLocationChangeListener.clear();
    }
    public interface LocationChangeListener{
        void onLocationChange(BigDecimal latitude,BigDecimal longitude,String title,String city);
    }
}
