package com.campussay.carpool.wxapi;

/**
 * creat by teng on 2019/4/22
 */
public class LoginBean {

    /**
     * code : 0
     * data : {"nickName":"初见、","sex":0,"headUrl":"http://thirdwx.qlogo.cn/mj4h1CCgcnQ/132",
     * "id":"100,"token":"d88d32bd285a4872ab78de0995c26aa1"}
     * success : true
     * message : 登录成功
     */

    private int code;
    private DataBean data;
    private boolean success;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class DataBean {
        /**
         * nickName : 初见、
         * sex : 0
         * headUrl : http://thirdwx.qlogo.cn/mj4h1CCgcnQ/132
         * id : 100
         * token : d88d32bd285a4872ab78de0995c26aa1
         */

        private String nickName;
        private int sex;
        private String headUrl;
        private String id;
        private String token;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
