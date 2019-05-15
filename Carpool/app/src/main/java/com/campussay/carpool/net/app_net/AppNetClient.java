package com.campussay.carpool.net.app_net;

/**
 * create by zuyuan on 2019/4/4
 */
public class AppNetClient {

    public static final String BASE_URL = "https://pin.varbee.com/";

    public static ApiService getInstance() {
        return Instance.INSTANCE;
    }

    private static class Instance {
        static final ApiService INSTANCE = new GeneralServiceProvider()
                .getService(BASE_URL, new GeneralOKClient()
                        .setInterceptor(new GeneralInterceptor()));
    }

    /*
      用法:
      AppNetClient.getInstance()
                  .observable(path)
                  .compose(RxSchedulers.<T>applySchedulers())        //切换线程
                  .compose(this.bindUntilEvent(ActivityEvent.STOP))  //绑定在哪一个方法上取消订阅
                  .subscribe(...)

      ExceptionUtil:专门解析Observer的error(Throwable e)参数，以判断是哪一种错误
     */
}
