package com.campussay.carpool.ui.self.testbean;

import java.util.List;

/**
 * Create by Zhangfan on 2019/4/19
 **/
public class FriendBean {


    public static class Data {
        /**
         * nickName : zmj
         * headUrl : https://www.woyaogexing.com/touxiang/katong/2019/766635.html
         * id : 46
         * teams : [{"teamId":"66","startName":"测试起点详情6","endName":"测试终点详情6","destinationTime":"标准时间戳"},{"teamId":"68","startName":"测试起点详情6","endName":"测试终点详情6","destinationTime":"标准时间戳"}]
         */

        private String nickName;
        private String headUrl;
        private String id;
        private List<TeamsBean> teams;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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

        public List<TeamsBean> getTeams() {
            return teams;
        }

        public void setTeams(List<TeamsBean> teams) {
            this.teams = teams;
        }

        public static class TeamsBean {
            /**
             * teamId : 66
             * startName : 测试起点详情6
             * endName : 测试终点详情6
             * destinationTime : 标准时间戳
             */

            private String teamId;
            private String startName;
            private String endName;
            private String destinationTime;

            public String getTeamId() {
                return teamId;
            }

            public void setTeamId(String teamId) {
                this.teamId = teamId;
            }

            public String getStartName() {
                return startName;
            }

            public void setStartName(String startName) {
                this.startName = startName;
            }

            public String getEndName() {
                return endName;
            }

            public void setEndName(String endName) {
                this.endName = endName;
            }

            public String getDestinationTime() {
                return destinationTime;
            }

            public void setDestinationTime(String destinationTime) {
                this.destinationTime = destinationTime;
            }
        }
    }
}
