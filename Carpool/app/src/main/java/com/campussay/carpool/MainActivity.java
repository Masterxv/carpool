package com.campussay.carpool;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.campussay.carpool.net.RxSchedulers;
import com.campussay.carpool.net.app_net.AppNetClient;
import com.campussay.carpool.room.RoomHelper;
import com.campussay.carpool.room.TestEntity;
import com.campussay.carpool.ui.base.BaseActivity;
import com.campussay.carpool.utils.LogUtils;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity<MainPresenter> {
    TextView textView;

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getDataFromRoomAndShow() {
//        RoomHelper.getInstance().openCarpoolDatabase()
//                .getDatabase().getDAO().queryEntities().observe(this, new Observer<List<TestEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<TestEntity> testEntities) {
//                for (TestEntity testEntity:testEntities){
//                    textView.append(testEntity.getName()+"\n");
//                }
//            }
//        });
    }

    @Override
    public void initView() {

        textView = findViewById(R.id.test);

        //测试数据库的例子
        TestEntity demoEntity = new TestEntity();
        demoEntity.setName("dnasdasdas");
        demoEntity.setAge(1465);
        RoomHelper.getInstance().openCarpoolDatabase().getDatabase().getDAO().insertEntity(demoEntity);
        /*TestEntity demoEntity1 =  RoomHelper.getInstance().querySimple().get(0);
        for(TestEntity testEntity:RoomHelper.getInstance().querySimple()){
            textView.append(testEntity.getPhone_num()+"\n");
        }

       /*RoomHelper.getInstance().upgrade("ALTER TABLE users2 ADD COLUMN phone_num TEXT");

        TestEntity testEntity = new TestEntity();
        testEntity.setName("wang132");
        testEntity.setAge(1123);
        testEntity.setPhone_num("123");
        RoomHelper.getInstance().openCarpoolDatabase().insert(testEntity);
        LogUtils.d(""+RoomHelper.getInstance().querySimple().get(1).getPhone_num());*/

    }

    public void fragment(View view){
        LogUtils.d("出碎片");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranction = fragmentManager.beginTransaction();
        tranction.replace(R.id.fragment_main,new MainFragment());
        tranction.commit();

    }

    public void onClick1(View view) {
        requestLogin("123", "小鹦鹉",
                "https://image-1255939184.cos.ap-chongqing.myqcloud.com/TIM图片20190406125154.jpg",
                0);
    }

    public void onClick2(View view) {
        requestLogin("456", "tiger",
                "https://image-1255939184.cos.ap-chongqing.myqcloud.com/TIM图片20190406125154.jpg",
                1);
    }

    public void onClick3(View view) {
        requestLogin("123", "小鹦鹉",
                "https://image-1255939184.cos.ap-chongqing.myqcloud.com/apple.jpg",
                3);
    }

    private void requestLogin(String code, String name, String url, int gender) {
        RequestBean bean = new RequestBean();
        bean.code = code;
        bean.min = 1;
        RequestBean.UserBean user = new RequestBean.UserBean();
        user.avatarUrl = url;
        user.gender = gender;
        user.nickName = name;
        user.phoneId = "0";
        bean.user = user;
        Gson gson = new Gson();
        String gs = gson.toJson(bean);

        RequestBody body = RequestBody.create(okhttp3.MediaType
                .parse("application/json; charset=utf-8"), gs);

        AppNetClient.getInstance()
                .loginReceived(body)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<ReceivedBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ReceivedBean receivedBean) {
                        if (receivedBean.success) {
                            int id = Integer.valueOf(receivedBean.getData().getUid());
                            String token = receivedBean.getData().getToken();
                            CarpoolApplication.setAccount(new CarpoolApplication
                                    .Account(id, token, name, url, gender));
                            Toast.makeText(MainActivity.this, "登录成功SUCCESS", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "登录失败ERROR", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "登录失败ERROR", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onToClick(View view) {
        startActivity(new Intent(this, ShellActivity.class));
    }

    public static class RequestBean {

        /**
         * code : 733abab7-43c8-40c2-96e6-b1b947ca21f1
         * min : 0
         * user : {"nickName":"tom","avatarUrl":"www.baidu.com","gender":0,"phoneId":"12345678"}
         */

        private String code;
        private int min;
        private UserBean user;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * nickName : tom
             * avatarUrl : www.baidu.com
             * gender : 0
             * phoneId : 12345678
             */

            private String nickName;
            private String avatarUrl;
            private int gender;
            private String phoneId;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getPhoneId() {
                return phoneId;
            }

            public void setPhoneId(String phoneId) {
                this.phoneId = phoneId;
            }
        }
    }

    public static class ReceivedBean {

        /**
         * success : true
         * code : 0
         * message : 登录成功
         * data : {"uid":"50","token":"b4b323c577174ea692460a18b9658d59"}
         */

        private boolean success;
        private int code;
        private String message;
        private DataBean data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * uid : 50
             * token : b4b323c577174ea692460a18b9658d59
             */

            private String uid;
            private String token;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
