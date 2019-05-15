package com.campussay.carpool.ui.recommend;

import java.util.List;

/**
 * creat by teng on 2019/4/17
 */

public class RecommendDataBean{


    /**
     * briefTeamDTOS : [{"id":"516","leaderName":"初见、","originName":"重庆邮电大学","destinationName":"重庆华峰化工有限公司","targetTime":"2019-05-24T22:05:28"},{"id":"598","leaderName":"_ban","originName":"重庆邮电大学","destinationName":"菜园坝","targetTime":"2019-05-27T01:09:59"},{"id":"627","leaderName":"初见、","originName":"重庆邮电大学","destinationName":"永川区","targetTime":"2019-05-26T18:56:53"},{"id":"611","leaderName":"jeg tror, du","originName":"重庆邮电大学","destinationName":"洪崖洞","targetTime":"2019-04-27T20:00:23"},{"id":"623","leaderName":"ZZY","originName":"重庆邮电大学(重庆市南岸区崇文路2号)","destinationName":"重庆邮电大学(重庆市南岸区崇文路2号)","targetTime":"2019-04-27T18:00:12"}]
     * onlineNum : 5
     */

    private int onlineNum;
    private List<BriefTeamDTOSBean> briefTeamDTOS;

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public List<BriefTeamDTOSBean> getBriefTeamDTOS() {
        return briefTeamDTOS;
    }

    public void setBriefTeamDTOS(List<BriefTeamDTOSBean> briefTeamDTOS) {
        this.briefTeamDTOS = briefTeamDTOS;
    }

    public static class BriefTeamDTOSBean {
        /**
         * id : 516
         * leaderName : 初见、
         * originName : 重庆邮电大学
         * destinationName : 重庆华峰化工有限公司
         * targetTime : 2019-05-24T22:05:28
         */

        private String id;
        private String leaderName;
        private String originName;
        private String destinationName;
        private String targetTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLeaderName() {
            return leaderName;
        }

        public void setLeaderName(String leaderName) {
            this.leaderName = leaderName;
        }

        public String getOriginName() {
            return originName;
        }

        public void setOriginName(String originName) {
            this.originName = originName;
        }

        public String getDestinationName() {
            return destinationName;
        }

        public void setDestinationName(String destinationName) {
            this.destinationName = destinationName;
        }

        public String getTargetTime() {
            return targetTime;
        }

        public void setTargetTime(String targetTime) {
            this.targetTime = targetTime;
        }
    }
}
