package com.campussay.carpool.net.app_net;

import com.campussay.carpool.wxapi.LoginBean;
import com.campussay.carpool.MainActivity;
import com.campussay.carpool.net.BaseBean;
import com.campussay.carpool.ui.chat.bean.SelfChatBean;
import com.campussay.carpool.ui.post.GetRouteBean;
import com.campussay.carpool.ui.route.bean.ApplyBean;
import com.campussay.carpool.ui.recommend.RecommendDataBean;
import com.campussay.carpool.ui.recommend.RecommendPeopleNumBean;
import com.campussay.carpool.ui.route.RouteInformationBean;
import com.campussay.carpool.ui.self.testbean.FriendBean;
import com.campussay.carpool.ui.self.testbean.MessagesBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * create by zuyuan on 2019/4/4
 */
public interface ApiService {
    /*
     规范: 1.BASE_URI 应该使用的同一个URI
          2.种子应该都建立在BaseBean之上，若服务器返回报错json字符串，那么bean.getData()返回null
            若服务器返回成功json字符串，bean.getData()就应该是目标的种子类数据。
     bug: 不能使用继承自Observale的订阅者，会失败，因此只能使用原生Observable
     */

    /**
     * 请求私聊历史消息记录
     */
    @POST("api/tadpole/message/getHistroryMessage")
    Observable<BaseBean<List<SelfChatBean>>> selfChatRecord(@Header("token") String token,
                                                            @Query("uid") Long friendId,
                                                            @Query("size") int size,
                                                            @Query("messageId") Long msgId);

    /**
     * 测试发布行程
     */
    @Headers("{\"Content-Type: application/json\",\"Accept: application/json\"}")
    @POST("api/tadpole/team/releaseTeam")
    Observable<BaseBean<Boolean>> postRoute(@Header("token") String token, @Body RequestBody body);


    /**
     * 测试获取相似行程
     */
    @Headers("{\"Content-Type: application/json\",\"Accept: application/json\"}")
    @POST("api/tadpole/team/getSimilarTeam")
    Observable<BaseBean<List<GetRouteBean>>> getSimilarRoute(@Header("token") String token,
                                                             @Body RequestBody request);

    /**
     *测试加入队伍
     */
    @GET("api/tadpole/team/applyJoinTeam")
    Observable<BaseBean<Boolean>> joinTeam(@Header("token") String token,
                                                             @Query("teamId")String teamId);

    @Headers("{\"Content-Type: application/json\",\"Accept: application/json\"}")
    @POST("api/tadpole/user/login")
    Observable<MainActivity.ReceivedBean> loginReceived(@Body RequestBody gson);


    /*
     * 请求我的页好友列表
     *
     * */
    @GET("/api/tadpole/user/getFriendList")
    Observable<BaseBean<List<FriendBean.Data>>> getSelfFriendsList(@Header("token") String token);

    /*
     * 我的页未读消息列表
     * */
    @GET("/api/tadpole/message/getUnReadNum")
    Observable<BaseBean<List<MessagesBean.Data>>> getSelfMessagesList(@Header("token") String token);

    /*
    * 行程页申请列表
    * */
    @GET("/api/tadpole/team/getAllIntentTeam")
    Observable<BaseBean<List<ApplyBean.Data>>> getApplyList(@Header("token") String token,
                                                            @Query("page") int page);

    /**
     * 推荐行程
     */

    @GET("/api/tadpole/team/teamRecommend")
    Observable<BaseBean<RecommendDataBean>> getRecommendList(@Header("token") String token);


    /**
     * 在线人数
     */
    @GET("/api/tadpole/message/getOnlineNumber")
    Observable<RecommendPeopleNumBean> getPeopleOnLine(@Header("token") String token);

    /**
     * 上传位置信息
     */
    @POST("/api/tadpole/user/updateUserLocation")
    Observable<ResponseBody> postLocation(@Header("token") String token,
                                          @Body RequestBody requestBody);

    /**S
     * 行程信息
     */

    @GET("/api/tadpole/team/getTeamInfoList")
    Observable<BaseBean<List<RouteInformationBean.dataBean>>> getRouteList(@Header("token") String token);

    /**
     * 取消行程
     */

    @GET("/api/tadpole/team/cancelTeam")
    Observable<BaseBean> cancelRoute(@Header("token") String token,
                                     @Query("teamId") Long teamId);

    /**
     * 申请同意名单
     */

    @POST("/api/tadpole/team/agreeTeam")
    Observable<BaseBean> applyRoute(@Header("token") String token,
                                        @Body RequestBody requestBody);

    /**
     *推荐页申请加入行程
     */

    @GET("/api/tadpole/team/applyJoinTeam")
    Observable<BaseBean> recommendApplyRoute(@Header("token") String token,
                                     @Query("teamId") Integer teamId);

    /**
     * 行程完结
     */
    @GET("/api/tadpole/team/evaluateTeam")
    Observable<BaseBean> routeOver(@Header("token") String token,
                                             @Query("teamId") Integer teamId,
                                             @Query("evaluate")int evaluate);

    /**
     * 登录
     */

    @POST("/api/tadpole/user/loginAndroid")
    Observable<LoginBean> login(@Query("code")  String code);
}
