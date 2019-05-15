package com.campussay.carpool.ui.route;

import java.util.List;

/**
 * creat by teng on 2019/4/17
 */
public class RouteInformationBean {
    public static class dataBean {

        /**
         * teamId : 1
         * startName : 重庆邮电大学
         * endName : 重庆北站
         * datetime : 2019/4/10 08:50
         * leader : {"name":"陈东南","photo":"http://pic.sogou.com/d?query=头像&page=1","sex":1,"userId":"1"}
         * member : {"name":"李彬楷","photo":"http://pic.sogou.com/d?query=头像&page=1","sex":1,"userId":"2"}
         * finshed : 0
         * isLeader : true
         * evaluate : 0
         * applyList : [{"datetime":"2019-04-11T19:17:57","userVO":{"name":"楷哥","photo":"http://pic.sogou.com/d?query=头像&page=1","sex":1,"userId":"3"}},{"datetime":"2019-04-11T19:17:57","userVO":{"name":"斌哥","photo":"http://pic.sogou.com/d?query=头像&page=1","sex":1,"userId":"4"}}]
         */

        private String teamId;
        private String startName;
        private String endName;
        private String datetime;
        private LeaderBean leader;
        private MemberBean member;
        private int finshed;
        private boolean isLeader;
        private int evaluate;
        private List<ApplyListBean> applyList;

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

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public LeaderBean getLeader() {
            return leader;
        }

        public void setLeader(LeaderBean leader) {
            this.leader = leader;
        }

        public MemberBean getMember() {
            return member;
        }

        public void setMember(MemberBean member) {
            this.member = member;
        }

        public int getFinshed() {
            return finshed;
        }

        public void setFinshed(int finshed) {
            this.finshed = finshed;
        }

        public boolean isIsLeader() {
            return isLeader;
        }

        public void setIsLeader(boolean isLeader) {
            this.isLeader = isLeader;
        }

        public int getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(int evaluate) {
            this.evaluate = evaluate;
        }

        public List<ApplyListBean> getApplyList() {
            return applyList;
        }

        public void setApplyList(List<ApplyListBean> applyList) {
            this.applyList = applyList;
        }

        public static class LeaderBean {
            /**
             * name : 陈东南
             * photo : http://pic.sogou.com/d?query=头像&page=1
             * sex : 1
             * userId : 1
             */

            private String name;
            private String photo;
            private int sex;
            private String userId;

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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

        public static class MemberBean {
            /**
             * name : 李彬楷
             * photo : http://pic.sogou.com/d?query=头像&page=1
             * sex : 1
             * userId : 2
             */

            private String name;
            private String photo;
            private int sex;
            private String userId;

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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

        public static class ApplyListBean {
            /**
             * datetime : 2019-04-11T19:17:57
             * userVO : {"name":"楷哥","photo":"http://pic.sogou.com/d?query=头像&page=1","sex":1,"userId":"3"}
             */

            private String datetime;
            private UserVOBean userVO;

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public UserVOBean getUserVO() {
                return userVO;
            }

            public void setUserVO(UserVOBean userVO) {
                this.userVO = userVO;
            }

            public static class UserVOBean {
                /**
                 * name : 楷哥
                 * photo : http://pic.sogou.com/d?query=头像&page=1
                 * sex : 1
                 * userId : 3
                 */

                private String name;
                private String photo;
                private int sex;
                private String userId;

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

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }
            }
        }
    }
}