package com.campussay.carpool.ui.self.testbean;


import java.util.List;

/**
 * Create by Zhangfan on 2019/4/9
 **/
public class MessagesBean  {


    public static class Data {
        /**
         * unReadNum : 3
         * lastContent : test last
         * theme : tom
         * type : 0
         * id : 2
         * headUrl : www.baidu.com
         * sencondTheam : 测试终点详情3
         */

        private int unReadNum;
        private String lastContent;
        private String theme;
        private int type;
        private String id;
        private String headUrl;
        private String sencondTheam;

        public int getUnReadNum() {
            return unReadNum;
        }

        public void setUnReadNum(int unReadNum) {
            this.unReadNum = unReadNum;
        }

        public String getLastContent() {
            return lastContent;
        }

        public void setLastContent(String lastContent) {
            this.lastContent = lastContent;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getSencondTheam() {
            return sencondTheam;
        }

        public void setSencondTheam(String sencondTheam) {
            this.sencondTheam = sencondTheam;
        }
    }
}
