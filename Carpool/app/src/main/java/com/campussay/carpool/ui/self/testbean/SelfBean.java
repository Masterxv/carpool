package com.campussay.carpool.ui.self.testbean;

import com.campussay.carpool.net.BaseBean;

import java.util.List;

/**
 * Create by Zhangfan on 2019/4/9
 **/
public class SelfBean extends BaseBean<SelfBean.Data> {

    public class Data {
    /*
    * "uid": "50", //当前用户id
		"token": "b4b323c577174ea692460a18b9658d59"
    * */
    private String uid;
    private String token;

        public String getToken() {
            return token;
        }

        public String getUid() {
            return uid;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
