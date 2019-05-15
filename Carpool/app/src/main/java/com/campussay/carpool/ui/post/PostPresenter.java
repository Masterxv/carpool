package com.campussay.carpool.ui.post;

import android.arch.persistence.room.util.StringUtil;
import android.util.Log;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.ShellActivity;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.net.app_net.RequestErrorBean;
import com.campussay.carpool.net.app_net.RequestErrorException;
import com.campussay.carpool.ui.base.BasePresenter;
import com.campussay.carpool.utils.LogUtils;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * create by WenJinG on 2019/4/6
 */
public class PostPresenter extends BasePresenter<PostFragment> implements  PoiSearch.OnPoiSearchListener {
    public  List<PostRecyclerViewItem>items = new ArrayList<>();
    public List<PostSearchRecyclerViewItem>searchItems = new ArrayList<>();
    private String cityName ="";
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiResult result;
    private static final int page = 0;
    private List<PoiItem>poiItemList = new ArrayList<>();


    public void startPoiSearch(String keyword){
        if(mView.getCity()!=null)
        cityName = mView.getCity();
        if(mView.getCity()!=null)
        query = new PoiSearch.Query(keyword,"",cityName);
        if(query!=null){
        query.setPageSize(10);
        query.setPageNum(page);
            }
            poiSearch = new PoiSearch(mView.getContext(), query);
        if(poiSearch!=null) {
            poiSearch.setOnPoiSearchListener(this);
            //mView.showToast("正在搜索，请稍等......");
            poiSearch.searchPOIAsyn();
        }
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        searchItems.clear();
        LogUtils.d("搜索返回码(1000正常)" + i);
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if(poiResult != null && poiResult.getQuery()!=null){
                if(poiResult.getQuery().equals(query)){
                    //poiResult.getPois()可以获取到PoiItem列表
                    result = poiResult;
                    List<PoiItem> poiItemList = poiResult.getPois();
                    this.poiItemList = poiResult.getPois();
                    for(PoiItem item:poiItemList){
                        PostSearchRecyclerViewItem item1 = new PostSearchRecyclerViewItem(
                                item.getTitle(),item.getSnippet());
                        searchItems.add(item1);
                    }
                    if(mView.startSearch) {
                        initOriginRouteData(mView.routeData, 0);
                    }
                    if(mView.endSearch) {
                        initDestinationRouteData(mView.routeData, 0);
                    }

                }
            }else {
                mView.showToast("无搜索结果");
            }
        }else {
            //mView.showToast("搜索错误");
        }
        mView.searchItemChange();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void postRoute(PostRouteData routeData){
        Gson gson = new Gson();
        String gs = gson.toJson(routeData);

        RequestBody body = RequestBody.create(okhttp3.MediaType
                .parse("application/json; charset=utf-8"), gs);
        AppNetClient.getInstance().postRoute(CarpoolApplication.getAccount().getToken(), body)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<BaseBean<Boolean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseBean<Boolean> booleanBaseBean) {
                if(!booleanBaseBean.getData()){
                    mView.showToast("你已加入相似行程");
                    return;
                }
                if(booleanBaseBean.getData()) {
                    mView.showToast("发布成功，快去管理你的行程吧！");
                    mView.clearEditText();
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.showToast("发布信号没找到，再试试吧");
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void initOriginRouteData(PostRouteData routeData,int position){
        if(poiItemList.size()!=0) {
            routeData.setOriginLatitude(BigDecimal.valueOf(poiItemList.get(position).getLatLonPoint().getLatitude()));
            routeData.setOriginLongitude(BigDecimal.valueOf(poiItemList.get(position).getLatLonPoint().getLongitude()));
            routeData.setOriginAddressDetail(poiItemList.get(position).getSnippet());
            routeData.setOriginName(poiItemList.get(position).getTitle());
        }
    }

    public void initDestinationRouteData(PostRouteData routeData,int position){
        if(poiItemList.size()!=0) {
            routeData.setDestinationLatitude(BigDecimal.valueOf(poiItemList.get(position).getLatLonPoint().getLatitude()));
            routeData.setDestinationLongitude(BigDecimal.valueOf(poiItemList.get(position).getLatLonPoint().getLongitude()));
            routeData.setDestinationAddressDetail(poiItemList.get(position).getSnippet());
            routeData.setDestinationName(poiItemList.get(position).getTitle());
        }
    }


    public void getSimilarRoute(PostRouteData routeData){
        items.clear();
        PostRequestBean requestBean = new PostRequestBean();
        requestBean.setOriginLongitude(routeData.getOriginLongitude());
        requestBean.setOriginLatitude(routeData.getOriginLatitude());
        requestBean.setDestinationLongitude(routeData.getDestinationLongitude());
        requestBean.setDestinationLatitude(routeData.getDestinationLatitude());
        Gson gson = new Gson();
        String gs = gson.toJson(requestBean);

        RequestBody body = RequestBody.create(okhttp3.MediaType
                .parse("application/json; charset=utf-8"), gs);

        AppNetClient.getInstance().getSimilarRoute(CarpoolApplication.getAccount().getToken(),body)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<BaseBean<List<GetRouteBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseBean<List<GetRouteBean>> listBaseBean) {
                if(listBaseBean.getData().size()==0){
                    mView.showToast("暂时没有该行程，快去发布吧！！！");
                    mView.onDataChange();
                    return;
                }
                for(GetRouteBean getRouteBean:listBaseBean.getData()){
                    LogUtils.d("GET ROUTE BEAN 请求返回值"+getRouteBean.toString());
                    Date date = new Date(Long.valueOf(getRouteBean.getTargetTime()));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
                    df.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    items.add(new PostRecyclerViewItem(getRouteBean.getOriginName(),
                            getRouteBean.getDestinationName(), df.format(date),
                            getRouteBean.getLeaderName(),getRouteBean.getId()));
                }
                mView.onDataChange();
            }

            @Override
            public void onError(Throwable e) {
                mView.showToast("输入信息有误");
                mView.onDataChange();
                if(e instanceof RequestErrorException)
                LogUtils.d("获取相似行程出错信息："+((RequestErrorException) e).getErrorCode());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void joinTeam(String teamId){
        String reason;
        AppNetClient.getInstance().joinTeam(CarpoolApplication.getAccount().getToken(),teamId)
                .compose(RxSchedulers.applySchedulers()).subscribe(new Observer<BaseBean<Boolean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseBean<Boolean> booleanBaseBean) {
                if(!booleanBaseBean.getData()){
                    mView.showToast("你已经申请过该行程");
                    return;
                }
                mView.showToast("申请成功");
                if(!"".equals(mView.routeData.getOriginName()))
                getSimilarRoute(mView.routeData);
            }

            @Override
            public void onError(Throwable e) {
                mView.showToast("申请失败");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
