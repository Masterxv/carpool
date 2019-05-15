package com.campussay.carpool.ui.route.bean;

import com.campussay.carpool.net.BaseBean;

/**
 * Create by Zhangfan on 2019/4/12
 **/
public class ApplyBean extends BaseBean<ApplyBean.Data> {
    public static class Data {

        /**
         * teamId : 748
         * origin : 重庆邮电大学
         * destination : 南坪街道响水路社区
         * leaderInfo : {"userId":"149","name":"_ban","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIRdRic7Jujz7eRV4drb3H0FUaYZHBFI70gicrQEPicGufWJgB4bj6BOHqv8XJIvhngj1XpzTNC3fSNg/132"}
         * targetTime : 2019-06-27T17:06:24
         * type : 1
         * hasNext : true
         */

        private String teamId;
        private String origin;
        private String destination;
        private LeaderInfoBean leaderInfo;
        private String targetTime;
        private int type;
        private boolean hasNext;

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public LeaderInfoBean getLeaderInfo() {
            return leaderInfo;
        }

        public void setLeaderInfo(LeaderInfoBean leaderInfo) {
            this.leaderInfo = leaderInfo;
        }

        public String getTargetTime() {
            return targetTime;
        }

        public void setTargetTime(String targetTime) {
            this.targetTime = targetTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public static class LeaderInfoBean {
            /**
             * userId : 149
             * name : _ban
             * photo : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIRdRic7Jujz7eRV4drb3H0FUaYZHBFI70gicrQEPicGufWJgB4bj6BOHqv8XJIvhngj1XpzTNC3fSNg/132
             */

            private String userId;
            private String name;
            private String photo;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }
    }
}
