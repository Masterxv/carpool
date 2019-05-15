package com.campussay.carpool.xg;

/**
 * create by zuyuan on 2019/4/21
 */
public class XGMessageBean {


    /**
     * code : 0
     * data : {"id":1068,"type":1,"title":"申请通过","content":"大足区->龙水镇","who":"勿忘"}
     * message : 消息推送
     * success : true
     */

    private int code;
    private DataBean data;
    private String message;
    private boolean success;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * id : 1068
         * type : 1
         * title : 申请通过
         * content : 大足区->龙水镇
         * who : 勿忘
         */

        private int id;
        private int type;
        private String title;
        private String content;
        private String who;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }

//    public static void main(String[] args) {
//        XGMessageBean bean = parse("");
//        System.out.println(bean.getData().type);
//        System.out.println(bean.getData().title);
//    }
}
