package com.campussay.carpool.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.LocationListener;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.math.BigDecimal;

import retrofit2.http.PUT;

/**
 * create by WenJinG on 2019/4/22
 */


public class Location implements AMapLocationListener{

    /* 用法

     Location.getInstance().configClient(this).setLocateListener(new Location.LocateListener() {
            @Override
            public void onLocate(BigDecimal latitude, BigDecimal longitude, String cityName, String title) {
            //第一个参数是纬度，第二个是经度，第三个是城市名，第四个是标志性建筑如：重庆邮电大学

            }
        });

        注意在生命周期变化时记得调用stopLocate()（当前页面不可见时，停止定位），destroyClient()（当前页面销毁时，销毁定位）方法
 */
    private  AMapLocationClient client;
    private  AMapLocationClientOption clientOption;
    private  String cityName = "";
    public   String title = "";
    private  BigDecimal currentLat;
    private  BigDecimal currentLon;
    private static Location instance;
    private LocateListener locateListener;


    public synchronized static Location getInstance() {
        if(instance==null){
            instance = new Location();
        }
        return instance;
    }

    public  Location configClient(Context context){
        client = new AMapLocationClient(context);
        client.setLocationListener(this);
        clientOption = new AMapLocationClientOption();
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //clientOption.setOnceLocation(true); 定位一次
        clientOption.setInterval(10000);
        clientOption.setLocationCacheEnable(false);
        clientOption.setMockEnable(false);
        if(client!=null){
            client.setLocationOption(clientOption);
            client.stopLocation();
            client.startLocation();
        }
        return this;
    }


    public Location setLocateListener(LocateListener locateListener) {
        this.locateListener = locateListener;
        return this;
    }

    public void stopLocate(){
        if(client!=null)
            client.stopLocation();
    }

    public void destroyClient(){
        if(client!=null)
            client.onDestroy();
    }

    public void startLocate(){
        if(client!=null)
            client.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                currentLat = BigDecimal.valueOf(aMapLocation.getLatitude());
                currentLon = BigDecimal.valueOf(aMapLocation.getLongitude());
                cityName = aMapLocation.getCity();
                title =aMapLocation.getPoiName();
                if(locateListener !=null){
                    locateListener.onLocate(currentLat,currentLon,cityName,title);
                }
                aMapLocation.getAccuracy();//获取精度信息
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogUtils.d( "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                LogUtils.d("定位错误："+aMapLocation.getErrorInfo());
            }
        }
    }


    public interface LocateListener{
        void onLocate(BigDecimal latitude,BigDecimal longitude,String cityName,String title);
    }

}
