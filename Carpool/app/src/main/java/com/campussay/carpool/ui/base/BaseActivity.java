package com.campussay.carpool.ui.base;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * create by WenJinG on 2019/4/4
 */
public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements IView,
        EasyPermissions.PermissionCallbacks {
    protected T mPresenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mPresenter = createPresenter();
        if(mPresenter!=null){
            mPresenter.attach(this);
        }
        toolbar = findViewById(R.id.toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            //toolbar.setNavigationIcon(R.drawable.ic_back1);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        getDataFromRoomAndShow();
        initView();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //权限请求成功
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //权限请求失败
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,
                grantResults, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detach();
        }
    }

    //在用户没有网络连接的时候调用
    @Override
    public void onNetworkDisconnected() {

    }

    //用于子Activity覆写，查询数据库中的内容直接更新ui
    protected void getDataFromRoomAndShow(){
        //用法：根据不同的情况观察不同的实体对应的LiveData，所以留给子类覆写此方法
        /*RoomHelper.getInstance().openCarpoolDatabase()
                .querySimple().observe(this, new Observer<List<TestEntity>>() {
            @Override
            public void onChanged(@Nullable List<TestEntity> testEntities) {
                for (TestEntity testEntity:testEntities){
                    //获取到数据testEntities更新ui
                }
            }
        });*/
    }

    public void setToolBarTitle(String s){
        TextView title = toolbar.findViewById(R.id.title);
        title.setText(s);
    }

    public abstract T createPresenter();
    public abstract int getContentViewId();
    public abstract void initView();

}
